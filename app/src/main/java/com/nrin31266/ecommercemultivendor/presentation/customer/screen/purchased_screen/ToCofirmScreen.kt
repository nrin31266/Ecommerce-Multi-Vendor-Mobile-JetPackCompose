package com.nrin31266.ecommercemultivendor.presentation.customer.screen.purchased_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.components.purchased.EmptySection
import com.nrin31266.ecommercemultivendor.presentation.components.purchased.SellerOrderCardItem
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
        confirmState.value.toConfirmList?.isEmpty() == true -> {
            EmptySection()
        }
        else -> {
            val render = confirmState.value.toConfirmList
            LazyColumn (modifier = Modifier.fillMaxSize()){
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