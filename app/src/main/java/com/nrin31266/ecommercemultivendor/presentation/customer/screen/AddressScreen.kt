package com.nrin31266.ecommercemultivendor.presentation.customer.screen



import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.components.address.AddressCardItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.share.SharedAddressViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.ButtonType
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomButton
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType

@Composable
fun AddressScreen(
    navController: NavController,
    sharedAddressViewModel: SharedAddressViewModel
) {
    val state = sharedAddressViewModel.addressState.collectAsStateWithLifecycle()
    val sharedAddressState = sharedAddressViewModel.sharedAddressState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        sharedAddressViewModel.getAllUserAddresses()
        Log.d("AddressScreen", sharedAddressState.value.selectedAddress.toString())
    }

    Scaffold (
        topBar = {
            CustomTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Select address", style = MaterialTheme.typography.titleLarge)
                    }
                },
                actionIcon = Icons.Default.Info,
                onActionClick = {},
                hasDivider = true
            )
        },
        contentWindowInsets = WindowInsets(0)
    ){
        innerPadding ->
        Box (
            modifier = Modifier.padding(innerPadding)
        ){
            when{
                state.value.loading ->{
                    FullScreenLoading()
                }
                state.value.errorMessage != null ->{
                    CustomMessageBox(
                        message = state.value.errorMessage!!,
                        type = MessageType.ERROR
                    )
                }
                else ->{
                    val addresses = state.value.addresses
                    LazyColumn {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text("Total address: ${addresses.size}/5")
                            }
                        }


                        itemsIndexed(addresses){  i, it ->
                            AddressCardItem(
                                address = it,
                                onEditClick = {}, isSelected = sharedAddressState.value.selectedAddress?.id == it.id,
                                onClick = {
                                    sharedAddressViewModel.selectAddress(address = it)
                                    navController.popBackStack()
                                },
                                modifier = Modifier.padding(bottom = 8.dp),
                                notLast = i!=addresses.size-1,


                            )


                        }
                        item {
                            if(addresses.isNotEmpty()){
                                Divider(modifier = Modifier.padding(bottom = 8.dp))
                            }
                            CustomButton (
                                type = ButtonType.OUTLINED,
                                onClick = {
                                    navController.navigate(CustomerRoutes.AddEditAddressScreen.withPath(-1))
                                },
                                text = "Add new address",
                                modifier = Modifier.padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()).padding(
                                    top = 8.dp
                                ).padding(horizontal = 8.dp).fillMaxWidth(),
                                icon = Icons.Default.AddCircleOutline

                            )
                        }
                    }
                }
            }
        }
    }

}