package com.nrin31266.ecommercemultivendor.presentation.customer.screen.purchased_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.components.purchased.EmptySection
import com.nrin31266.ecommercemultivendor.presentation.components.purchased.PendingPaymentItem
import com.nrin31266.ecommercemultivendor.presentation.components.purchased.SellerOrderCardItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.PurchasedViewModel
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType

@Composable
fun PurchasedScreen(
    navController: NavController,
    purchasedViewModel: PurchasedViewModel
){
    val toPayState = purchasedViewModel.toPayState.collectAsStateWithLifecycle()

    when{
        toPayState.value.isLoading -> {
            FullScreenLoading()
        }
        toPayState.value.errorMessage != null ->{
            CustomMessageBox(
                message = toPayState.value.errorMessage!!,
                type = MessageType.ERROR
            )
        }
        toPayState.value.toPayList?.isEmpty() == true -> {
            EmptySection()
        }
        else -> {

            val render = toPayState.value.toPayList?: emptyList()
            LazyColumn (modifier = Modifier.fillMaxSize()){
                items(render) {
                    PendingPaymentItem(it, navController, purchasedViewModel)
                }
            }

        }
    }
}