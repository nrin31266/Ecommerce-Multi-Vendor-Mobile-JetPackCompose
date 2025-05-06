package com.nrin31266.ecommercemultivendor.presentation.customer.screen.purchased_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.components.purchased.EmptySection
import com.nrin31266.ecommercemultivendor.presentation.components.purchased.SellerOrderCardItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.PurchasedViewModel
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType

@Composable
fun DeliveredScreen(
    navController: NavController,
    purchasedViewModel: PurchasedViewModel
) {
    val deliveredState = purchasedViewModel.deliveredState.collectAsStateWithLifecycle()

    when{
        deliveredState.value.isLoading -> {
            FullScreenLoading()
        }
        deliveredState.value.errorMessage != null ->{
            CustomMessageBox(
                message = deliveredState.value.errorMessage!!,
                type = MessageType.ERROR
            )
        }
        deliveredState.value.deliveredList?.isEmpty() == true -> {
            EmptySection()
        }
        else -> {
            val render = deliveredState.value.deliveredList
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(render?.size ?: 0) {
                    SellerOrderCardItem(
                        sellerOrder = render!![it],
                        purchasedViewModel = purchasedViewModel,
                        navController
                    )
                }
            }

        }
    }
}