package com.nrin31266.ecommercemultivendor.presentation.customer.screen.purchased_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.components.purchased.EmptySection
import com.nrin31266.ecommercemultivendor.presentation.components.purchased.SellerOrderCardItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.PurchasedViewModel
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType

@Composable
fun ShippingScreen(
    navController: NavController,
    purchasedViewModel: PurchasedViewModel
) {
    val shippingState = purchasedViewModel.shippingState.collectAsStateWithLifecycle()

    when{
        shippingState.value.isLoading -> {
            FullScreenLoading()
        }
        shippingState.value.errorMessage != null ->{
            CustomMessageBox(
                message = shippingState.value.errorMessage!!,
                type = MessageType.ERROR
            )
        }
        shippingState.value.shippingList?.isEmpty() == true -> {
            EmptySection()
        }
        else -> {
            val render = shippingState.value.shippingList
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(render?.size ?: 0) {
                    SellerOrderCardItem(
                        sellerOrder = render!![it],
                        purchasedViewModel = purchasedViewModel
                    )
                }
            }

        }
    }
}