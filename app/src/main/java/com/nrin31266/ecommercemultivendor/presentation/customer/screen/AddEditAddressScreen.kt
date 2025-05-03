package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.domain.dto.AddressDto
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.share.AddressEvent
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.share.SharedAddressViewModel
import com.nrin31266.ecommercemultivendor.presentation.utils.ButtonType
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomButton
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTextField
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditAddressScreen(
    navController: NavController,
    addressId: Long?= -1L,
    navBackStackEntry: NavBackStackEntry,
    sharedAddressViewModel: SharedAddressViewModel = hiltViewModel(navBackStackEntry)
) {
    val state = sharedAddressViewModel.addressActionState.collectAsStateWithLifecycle()

    val name = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val street = remember { mutableStateOf("") }
    val ward = remember { mutableStateOf("") }
    val district = remember { mutableStateOf("") }
    val province = remember { mutableStateOf("") }
    val isDefault = remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        sharedAddressViewModel.addressEvent.collectLatest {
            when (it) {
                is AddressEvent.SentNav -> {
                    navController.popBackStack()
                }
                else -> {}
            }
        }
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
                        Text("${if(addressId!=-1L) "Update" else "Add new"} address", style = MaterialTheme.typography.titleLarge)
                    }
                },
                actionIcon = Icons.Default.Info,
                onActionClick = {},
                hasDivider = true
            )
        },
        bottomBar = {
            Box (
                modifier = Modifier.fillMaxWidth().padding(bottom =
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()).padding(horizontal = 8.dp)
            ){
                Divider()
                if(addressId == -1L){
                    CustomButton(
                        text = "Add new",
                        onClick = {
                            if(name.value.isNotEmpty() && phoneNumber.value.isNotEmpty() && street.value.isNotEmpty() &&
                                ward.value.isNotEmpty() && district.value.isNotEmpty() && province.value.isNotEmpty()){
                                sharedAddressViewModel.addUserAddress(
                                    AddressDto(
                                        name = name.value,
                                        phoneNumber = phoneNumber.value,
                                        street = street.value,
                                        ward = ward.value,
                                        district = district.value,
                                        province = province.value,
                                        isDefault = isDefault.value
                                    )
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        type = ButtonType.FILLED,
                        icon = Icons.Default.AddCircleOutline
                    )
                }else{
                    CustomButton(
                        text = "Add new",
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        type = ButtonType.FILLED,
                        loading = state.value.actionLoading,
                        enabled = !state.value.actionLoading

                    )
                }
            }
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
                    LazyColumn {
                        item{
                            Card (
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ){
                                Column(modifier = Modifier.padding(8.dp).fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    CustomTextField(
                                        value = name.value,
                                        onValueChange = {name.value = it},
                                        placeholder = "Name",
                                        modifier = Modifier.fillMaxWidth(),
                                        label = "Name",
                                        leadingIcon = Icons.Default.Person
                                    )
                                    CustomTextField(
                                        value = phoneNumber.value,
                                        onValueChange = {phoneNumber.value = it},
                                        placeholder = "Phone number",
                                        modifier = Modifier.fillMaxWidth(),
                                        label = "Phone number",
                                        leadingIcon = Icons.Default.Call
                                    )
                                    CustomTextField(
                                        value = province.value,
                                        onValueChange = {province.value = it},
                                        placeholder="Province",
                                        modifier = Modifier.fillMaxWidth(),
                                        label = "Province",
                                        leadingIcon = Icons.Default.LocationCity
                                    )

                                    CustomTextField(
                                        value = district.value,
                                        onValueChange = {district.value = it},
                                        placeholder = "District",
                                        modifier = Modifier.fillMaxWidth(),
                                        label = "District",
                                    )
                                    CustomTextField(
                                        value = ward.value,
                                        onValueChange = {ward.value = it},
                                        placeholder = "Ward",
                                        modifier = Modifier.fillMaxWidth(),
                                        label = "Ward",
                                    )
                                    CustomTextField(
                                        value = street.value,
                                        onValueChange = {street.value = it},
                                        placeholder = "Street",
                                        modifier = Modifier.fillMaxWidth(),
                                        label = "Street",
                                    )
                                    Row (
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                    ){
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Set as default address")
                                        }
                                        Switch(
                                            checked = isDefault.value,
                                            onCheckedChange = {
                                                isDefault.value = it
                                            }
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}