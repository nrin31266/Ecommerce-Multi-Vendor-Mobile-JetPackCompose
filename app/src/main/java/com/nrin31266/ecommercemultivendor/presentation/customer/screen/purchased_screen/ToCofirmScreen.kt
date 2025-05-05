package com.nrin31266.ecommercemultivendor.presentation.customer.screen.purchased_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.PurchasedViewModel
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType

@Composable
fun ToConfirmScreen(
    navController: NavController,
    purchasedViewModel: PurchasedViewModel
){
    val confirmState = purchasedViewModel.toConfirmState.collectAsStateWithLifecycle()

    Box{
        when{
            confirmState.value.isLoading -> {
                FullScreenLoading()
            }
            confirmState.value.errorMessage != null ->{
                CustomMessageBox(
                    message = confirmState.value.errorMessage!!,
                    type = MessageType.ERROR
                )
            }
            else -> {
                Text("To confirm")
            }
        }
    }
}