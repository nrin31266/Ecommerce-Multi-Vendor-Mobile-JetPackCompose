package com.nrin31266.ecommercemultivendor.presentation.customer.screen.purchased_screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.PurchasedViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PurchasedScreen(
    navController: NavController,
    defaultTabIndex: Int,
    purchasedViewModel: PurchasedViewModel = hiltViewModel()
) {


    val scope = rememberCoroutineScope()
    val state = purchasedViewModel.purchasedState.collectAsStateWithLifecycle()
    val confirmState = purchasedViewModel.toConfirmState.collectAsStateWithLifecycle()
    val pickupState = purchasedViewModel.toPickupState.collectAsStateWithLifecycle()
    val shippingState = purchasedViewModel.shippingState.collectAsStateWithLifecycle()
    val deliveredState = purchasedViewModel.deliveredState.collectAsStateWithLifecycle()
    val cancelledState = purchasedViewModel.cancelledState.collectAsStateWithLifecycle()
    val toPayState = purchasedViewModel.toPayState.collectAsStateWithLifecycle()
    val tabs = listOf("To pay", "To confirm", "To pickup", "Shipping", "Delivered", "Cancelled")

    val pagerState = rememberPagerState(initialPage = state.value.tabIndex ?: defaultTabIndex)

    LaunchedEffect(Unit) {
        purchasedViewModel.eventFlow.collectLatest {
            when (it) {
                is PurchasedViewModel.PurchasedEvent.PopToTab -> {
                    pagerState.animateScrollToPage(it.tabIndex)
                }
                is PurchasedViewModel.PurchasedEvent.PopToPayment -> {
                    navController.navigate(CustomerRoutes.PaymentWebViewScreen.withLink(Uri.encode(it.paymentLink)))
                }
            }
        }
    }

    // Khi user swipe tab → cập nhật lại ViewModel
    LaunchedEffect(pagerState.currentPage) {
        purchasedViewModel.selectTab(pagerState.currentPage)
    }

    // Gọi API theo từng tab
    LaunchedEffect(state.value.tabIndex) {
        when (state.value.tabIndex) {
            0 -> if(toPayState.value.toPayList == null) purchasedViewModel.getUserPendingPayments()
            1 -> if (confirmState.value.toConfirmList == null) purchasedViewModel.toConfirmAction()
            2 -> if (pickupState.value.toPickupList == null) purchasedViewModel.toPickupAction()
            3 -> if (shippingState.value.shippingList == null) purchasedViewModel.shippingAction()
            4 -> if (deliveredState.value.deliveredList == null) purchasedViewModel.deliveredAction()
            5 -> if (cancelledState.value.cancelledList == null) purchasedViewModel.cancelAction()
            // Thêm các case khác nếu cần
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = { navController.popBackStack() },
                title = "Purchased",
                onActionClick = {},
                actionIcon = Icons.Default.Message,
                extraContent = {
                    ScrollableTabRow(selectedTabIndex = pagerState.currentPage) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = { Text(title) }
                            )
                        }
                    }
                },
                modifier = Modifier.background(Color.White)
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        HorizontalPager(
            count = tabs.size,
            state = pagerState,
            modifier = Modifier
//                .background(Color.White)
                .padding(innerPadding),
        ) { page ->
            when (page) {
                0 -> PurchasedScreen(navController, purchasedViewModel)
                1 -> ToConfirmScreen(navController, purchasedViewModel)
                2 -> ToPickupScreen(navController, purchasedViewModel)
                3 -> ShippingScreen(
                    navController,
                    purchasedViewModel
                )
                4 -> DeliveredScreen(
                    navController,
                    purchasedViewModel
                )
                5 -> CancelledScreen(navController, purchasedViewModel)
            }
        }

    }
}
