package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.funutils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.presentation.components.address.AddressCardItem
import com.nrin31266.ecommercemultivendor.presentation.components.checkout.PaymentMethod
import com.nrin31266.ecommercemultivendor.presentation.components.checkout.PaymentMethodItem
import com.nrin31266.ecommercemultivendor.presentation.components.checkout.ShopCheckoutItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.CartViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.CheckoutEvent
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.CheckoutViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.SharedAddressViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.BasicNotification
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomButton
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType
import com.vanrin05.app.domain.PAYMENT_METHOD
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CheckoutScreen(
    navController: NavController,

    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
    sharedAddressViewModel: SharedAddressViewModel
) {


    val cartInfoState = cartViewModel.cartInfoState.collectAsStateWithLifecycle()
    val cartState = cartViewModel.state.collectAsStateWithLifecycle()
    val checkoutState = checkoutViewModel.checkoutState.collectAsStateWithLifecycle()
    val sharedAddressState = sharedAddressViewModel.sharedAddressState.collectAsStateWithLifecycle()
    val createOrderState = checkoutViewModel.createOrderState.collectAsStateWithLifecycle()
    val selectedPaymentMethod = createOrderState.value.paymentMethod
    val totalShippingCosts = (cartState.value.cart?.groups?.size!! * 30000).toLong()
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (sharedAddressState.value.selectedAddress == null) {
            sharedAddressViewModel.getDefaultUserAddress()
        }

        checkoutViewModel.checkoutEvent.collectLatest {
            when(it){
                is CheckoutEvent.PaymentSuccess -> {
                    cartViewModel.clearCart()

                    navController.navigate(CustomerRoutes.CustomerHomeScreen.route) {
                        popUpTo(0) { inclusive = false }
                        launchSingleTop = true
                    }
                }
                is CheckoutEvent.PaymentFailed -> {
                    showDialog.value = true
                }
                else -> {}
            }
        }
    }

    Scaffold(
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
                        Text("Checkout", style = MaterialTheme.typography.titleLarge)
                    }
                },
                actionIcon = Icons.Default.Info,
                onActionClick = {},
                hasDivider = true
            )
        },
        bottomBar = {
            Column(
                Modifier.padding(
                    bottom = WindowInsets.navigationBars.asPaddingValues()
                        .calculateBottomPadding()
                )
            ) {


                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            8.dp
                        ), horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.End)

                        ) {
                            Text("Total: ", color = Color.Gray)
                            Text(
                                CurrencyConverter.toVND(cartInfoState.value.totalSellingPrice + totalShippingCosts),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp,
                                maxLines = 1
                            )
                        }
//                        Row(
//                            horizontalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            Text(
//                                "Save: ${CurrencyConverter.toVND(cartInfoState.value.totalMrpPrice - cartInfoState.value.totalSellingPrice)}" +
//                                        " for ${cartInfoState.value.totalCartItemAvailable} item${if (cartInfoState.value.totalCartItemAvailable > 1) "s" else ""}",
//                                fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary,
//                                maxLines = 1, overflow = TextOverflow.Ellipsis
//                            )
//                        }

                    }
                    Column {
                        CustomButton(
                            text = "Payment",
                            onClick = {
                                checkoutViewModel.createOrder(
                                    sharedAddressState.value.selectedAddress?.id!!
                                )
                            },
                            enabled = sharedAddressState.value.selectedAddress != null
                                    && !createOrderState.value.isLoading,
                            loading = createOrderState.value.isLoading
                        )
                    }
                }
            }

        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                checkoutState.value.isLoading -> {
                    FullScreenLoading()
                }

                checkoutState.value.errorMessage != null -> {
                    CustomMessageBox(
                        message = checkoutState.value.errorMessage!!,
                        type = MessageType.ERROR
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 8.dp)
                                    .shadow(
                                        1.dp,
                                        spotColor = Color.LightGray
                                    )
                                    .clip(RoundedCornerShape(4.dp)),
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Column {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn, "",
                                            tint = colorResource(R.color.error_red)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .clickable {
                                                navController.navigate(CustomerRoutes.AddressScreen.route)
                                            }
                                            .clip(RoundedCornerShape(4.dp)),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            if (sharedAddressState.value.selectedAddress == null) {
                                                Row(
                                                    modifier = Modifier
                                                        .border(
                                                            1.dp,
                                                            Color.LightGray,
                                                            RoundedCornerShape(4.dp)
                                                        )
                                                        .fillMaxWidth()
                                                        .padding(8.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    verticalAlignment = Alignment.CenterVertically

                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.AddCircleOutline,
                                                        ""
                                                    )
                                                    Text("Select address")
                                                }
                                            } else {
                                                AddressCardItem(
                                                    address = sharedAddressState.value.selectedAddress!!,
                                                    isOnlyView = true
                                                )
                                            }
                                        }
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.fillMaxHeight(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowForwardIos,
                                                "",
                                                tint = colorResource(R.color.cool_gray),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }

                            }
                        }

                        cartState.value.cart?.let {
                            items(it.groups) {
                                ShopCheckoutItem(
                                    navController = navController,
                                    shopGroup = it
                                )

                            }
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 8.dp)
                                    .shadow(
                                        1.dp,
                                        spotColor = Color.LightGray
                                    )
                                    .clip(RoundedCornerShape(4.dp)),
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PriceChange, "",
                                                tint = colorResource(R.color.error_red)
                                            )
                                            Text(
                                                "Platform voucher",
                                            )
                                        }
                                        Row(
                                            modifier = Modifier,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            androidx.compose.material.Text(
                                                "View",
                                                color = Color.Gray
                                            )
                                            androidx.compose.material3.Icon(
                                                imageVector = Icons.Default.ArrowForwardIos, "",
                                                tint = Color.Gray, modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.LocalShipping, "",
                                                tint = colorResource(R.color.teal_700)
                                            )
                                            Text(
                                                "Shipping voucher",
                                            )
                                        }
                                        Row(
                                            modifier = Modifier,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            androidx.compose.material.Text(
                                                "View",
                                                color = Color.Gray
                                            )
                                            androidx.compose.material3.Icon(
                                                imageVector = Icons.Default.ArrowForwardIos, "",
                                                tint = Color.Gray, modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 8.dp)
                                    .shadow(
                                        1.dp,
                                        spotColor = Color.LightGray
                                    )
                                    .clip(RoundedCornerShape(4.dp)),
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Payment, "",
                                            )
                                            Text(
                                                "Payment method",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 16.sp
                                            )
                                        }
                                        Row(
                                            modifier = Modifier,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            androidx.compose.material.Text(
                                                "All",
                                                color = Color.Gray
                                            )
                                            androidx.compose.material3.Icon(
                                                imageVector = Icons.Default.ArrowForwardIos, "",
                                                tint = Color.Gray, modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                }
                                Divider()

                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    val paymentMethods = listOf(
                                        PaymentMethod(
                                            PAYMENT_METHOD.CASH_ON_DELIVERY,
                                            "Cash on delivery",
                                            R.drawable.cash_on_delivery
                                        ),
                                        PaymentMethod(
                                            PAYMENT_METHOD.VNPAY,
                                            "VNPAY",
                                            R.drawable.vnpay
                                        ),
                                        PaymentMethod(
                                            PAYMENT_METHOD.STRIPE,
                                            "Stripe",
                                            R.drawable.stripe
                                        ),
                                    )
                                    paymentMethods.forEach {
                                        PaymentMethodItem(
                                            it,
                                            selectedPaymentMethod == it.method,
                                            {
                                                checkoutViewModel.selectPaymentMethod(it.method)
                                            })
                                    }
                                }
                            }
                        }
                        item {

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 8.dp)
                                    .shadow(
                                        1.dp,
                                        spotColor = Color.LightGray
                                    )
                                    .clip(RoundedCornerShape(4.dp)),
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                ) {
                                    Text("Payment details",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp)
                                }
                                Divider()
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Row (
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text("Total price",
                                            fontSize = 14.sp,
                                            modifier = Modifier.weight(1f))
                                        Text(CurrencyConverter.toVND(cartInfoState.value.totalSellingPrice),
                                            fontSize = 14.sp,)

                                    }
                                    Row (
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text("Total Shipping Costs",
                                            fontSize = 14.sp,
                                            modifier = Modifier.weight(1f))
                                        Text(CurrencyConverter.toVND(totalShippingCosts),
                                            fontSize = 14.sp,)

                                    }
                                    Row (
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text("Reduced shipping",
                                            fontSize = 14.sp,
                                            modifier = Modifier.weight(1f))
                                        Text("-"+CurrencyConverter.toVND(0L),
                                            fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)

                                    }
                                    Row (
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text("Reduced platform voucher",
                                            fontSize = 14.sp,
                                            modifier = Modifier.weight(1f))
                                        Text("-"+CurrencyConverter.toVND(0L),
                                            fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)

                                    }
                                }
                                Divider()
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Row (
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text("Total payment",
                                            fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
                                            modifier = Modifier.weight(1f))
                                        Text(CurrencyConverter.toVND(cartInfoState.value.totalSellingPrice + totalShippingCosts),
                                            fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if(showDialog.value){
        BasicNotification({showDialog.value = false}, createOrderState.value.errorMessage?:"")
    }
}

