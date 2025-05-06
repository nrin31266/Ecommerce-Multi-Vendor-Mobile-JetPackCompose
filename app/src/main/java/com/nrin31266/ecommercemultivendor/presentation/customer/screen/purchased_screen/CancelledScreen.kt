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
fun CancelledScreen(
    navController: NavController,
    purchasedViewModel: PurchasedViewModel
)  {
    val cancelledState = purchasedViewModel.cancelledState.collectAsStateWithLifecycle()

    when{
        cancelledState.value.isLoading -> {
            FullScreenLoading()
        }
        cancelledState.value.errorMessage != null ->{
            CustomMessageBox(
                message = cancelledState.value.errorMessage!!,
                type = MessageType.ERROR
            )
        }
        cancelledState.value.cancelledList?.isEmpty() == true -> {
            EmptySection()
        }
        else -> {
            val render = cancelledState.value.cancelledList
            LazyColumn (
                modifier = Modifier.fillMaxSize()
            ){
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