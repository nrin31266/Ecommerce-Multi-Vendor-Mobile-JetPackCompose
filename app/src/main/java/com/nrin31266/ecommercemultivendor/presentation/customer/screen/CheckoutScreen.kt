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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.fununtils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.presentation.components.DiscountLabel
import com.nrin31266.ecommercemultivendor.presentation.components.address.AddressCardItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.CartViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.CheckoutViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.share.SharedAddressViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomButton
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType

@Composable
fun CheckoutScreen(
    navController: NavController,

    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
    sharedAddressViewModel: SharedAddressViewModel
) {


    val cartInfoState = cartViewModel.cartInfoState.collectAsStateWithLifecycle()
    val checkoutState = checkoutViewModel.checkoutState.collectAsStateWithLifecycle()
    val sharedAddressState = sharedAddressViewModel.sharedAddressState.collectAsStateWithLifecycle()

    LaunchedEffect (Unit){
        if(sharedAddressState.value.selectedAddress == null){
            sharedAddressViewModel.getDefaultUserAddress()
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
                        ), horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                CurrencyConverter.toVND(cartInfoState.value.totalSellingPrice),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 16.sp,
                                maxLines = 1
                            )
                            Text(
                                CurrencyConverter.toVND(cartInfoState.value.totalMrpPrice),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 14.sp,
                                textDecoration = TextDecoration.LineThrough,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            DiscountLabel(cartInfoState.value.discountPercentage)
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "Save: ${CurrencyConverter.toVND(cartInfoState.value.totalMrpPrice - cartInfoState.value.totalSellingPrice)}" +
                                        " for ${cartInfoState.value.totalCartItemAvailable} item${if (cartInfoState.value.totalCartItemAvailable > 1) "s" else ""}",
                                fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary,
                                maxLines = 1, overflow = TextOverflow.Ellipsis
                            )
                        }

                    }
                    Column {
                        CustomButton(
                            text = "Payment",
                            onClick = {

                            },
                            enabled = cartInfoState.value.totalCartItemAvailable == cartInfoState.value.totalCartItem
                        )
                    }
                }
            }

        },
        contentWindowInsets = WindowInsets(0)
    ){
        innerPadding ->
        Box( modifier = Modifier.padding(innerPadding)){
            when{
                checkoutState.value.isLoading ->{
                    FullScreenLoading()
                }
                checkoutState.value.errorMessage != null ->{
                    CustomMessageBox(
                        message = checkoutState.value.errorMessage!!,
                        type = MessageType.ERROR
                    )
                }
                else->{
                    LazyColumn {
                        item {
                            Card (
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ){
                                Row (
                                    modifier = Modifier.padding(8.dp)
                                ){
                                    Column {
                                        Icon(imageVector = Icons.Default.LocationOn, "",
                                            tint = colorResource(R.color.error_red)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.clickable {
                                            navController.navigate(CustomerRoutes.AddressScreen.route)
                                        }.clip(RoundedCornerShape(4.dp)),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            if (sharedAddressState.value.selectedAddress == null){
                                                Row (
                                                    modifier = Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(4.dp)).fillMaxWidth().padding(8.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    verticalAlignment = Alignment.CenterVertically

                                                ){
                                                    Icon(imageVector = Icons.Default.AddCircleOutline, "")
                                                    Text("Select address")
                                                }
                                            }else{
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
                                            Icon(imageVector = Icons.Default.ArrowForwardIos, "",
                                                tint = colorResource(R.color.cool_gray), modifier = Modifier.size(16.dp))
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
}