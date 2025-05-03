package com.nrin31266.ecommercemultivendor.presentation.customer.screen



import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
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
    navBackStackEntry: NavBackStackEntry,
    sharedAddressViewModel: SharedAddressViewModel = hiltViewModel(navBackStackEntry)
) {
    val state = sharedAddressViewModel.addressState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        sharedAddressViewModel.getAllUserAddresses()
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


                        items(addresses){

                        }
                        item {
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