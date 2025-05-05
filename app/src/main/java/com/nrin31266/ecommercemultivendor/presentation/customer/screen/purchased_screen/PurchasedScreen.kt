package com.nrin31266.ecommercemultivendor.presentation.customer.screen.purchased_screen

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.common.constant.SELLER_ORDER_STATUS
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.PurchasedViewModel
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar



@Composable
fun PurchasedScreen(
    navController: NavController,
    defaultTabIndex: Int,
    purchasedViewModel: PurchasedViewModel = hiltViewModel()
){
    val state = purchasedViewModel.purchasedState.collectAsStateWithLifecycle()
    val confirmState = purchasedViewModel.toConfirmState.collectAsStateWithLifecycle()
    val pickupState = purchasedViewModel.toPickupState.collectAsStateWithLifecycle()
    val shippingState = purchasedViewModel.shippingState.collectAsStateWithLifecycle()
    val deliveredState = purchasedViewModel.deliveredState.collectAsStateWithLifecycle()
    val cancelledState = purchasedViewModel.cancelledState.collectAsStateWithLifecycle()

    LaunchedEffect(defaultTabIndex) {
        purchasedViewModel.selectTab(defaultTabIndex)
    }

    LaunchedEffect (state.value.tabIndex) {

        when (state.value.tabIndex) {
            0 -> {}
            1 -> {

                if(confirmState.value.toConfirmList == null){
                    Log.d(TAG, "Tab index: ${state.value.tabIndex}")
                    purchasedViewModel.toConfirmAction()
                }
            }
            2 -> {
                if(pickupState.value.toPickupList == null){
                    purchasedViewModel.toPickupAction()
                }
            }
            3 -> {}
            4 -> {}
            5 -> {}
            else -> {}
        }
    }

    val tabs = listOf("To pay", "To confirm", "To pickup", "Shipping", "Delivered", "Cancelled")



    Scaffold  (
        topBar = {
            CustomTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
                title = "Purchased",
                onActionClick = {},
                actionIcon = Icons.Default.Message
            )
        },
        contentWindowInsets = WindowInsets(0)
    ){
        innerPadding ->
        LazyColumn(
            contentPadding = innerPadding
        ) {
            if(state.value.tabIndex!=null){
                item{
                    Column  {
                        ScrollableTabRow(selectedTabIndex = state.value.tabIndex!!) {
                            tabs.forEachIndexed { index, title ->
                                Tab (
                                    selected = state.value.tabIndex == index,
                                    onClick = {  purchasedViewModel.selectTab(index) },
                                    text = { Text(title) }
                                )
                            }
                        }

                        when (state.value.tabIndex) {
                            0 -> Text("To pay")
                            1 -> ToConfirmScreen(
                                navController = navController,
                                purchasedViewModel = purchasedViewModel
                            )
                            2 -> Text("To pickup")
                            3 -> Text("Shipping")
                            4 -> Text("Delivered")
                            5 -> Text("Cancelled")
                        }
                    }
                }
            }
        }
    }

}