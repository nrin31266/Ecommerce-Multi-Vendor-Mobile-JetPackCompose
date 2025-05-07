package com.nrin31266.ecommercemultivendor.presentation.components.purchased

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.constant.SELLER_ORDER_STATUS
import com.nrin31266.ecommercemultivendor.common.funutils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.domain.dto.OrderItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.SellerOrderDto
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.PurchasedViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.ButtonSize
import com.nrin31266.ecommercemultivendor.presentation.utils.ButtonType
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomButton
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomCard

@Composable
fun SellerOrderCardItem(
    sellerOrder: SellerOrderDto,
    purchasedViewModel: PurchasedViewModel,
    navController: NavController?=null
) {
    var status = sellerOrder.status
    val showMore = rememberSaveable(sellerOrder.id) { mutableStateOf(false) }


    CustomCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                "${sellerOrder.seller.businessDetails?.businessName}",
                modifier = Modifier.weight(1f)
            )
            if(sellerOrder.status == SELLER_ORDER_STATUS.CANCELLED){
                Text(
                    sellerOrder.cancelReason,
                    fontSize = 15.sp,
                    color = colorResource(R.color.error_red),
                    modifier = Modifier
                )
            }
        }
        Divider()

        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),

            ) {
            val itemShow: List<OrderItemDto> = if (showMore.value) {
                sellerOrder.orderItems ?: emptyList()
            } else {
                sellerOrder.orderItems?.firstOrNull()?.let { listOf(it) } ?: emptyList()
            }

            itemShow.map {
                OrderItem(
                    orderItem = it,
                    extraContent = {
                        if(!it.isRated && sellerOrder.status == SELLER_ORDER_STATUS.COMPLETED){
                            CustomButton(
                                backgroundColor = colorResource(R.color.elegant_gold),
                                onClick = {
                                    Log.d(TAG, "To rate order id: ${it.id} ")
                                    navController?.navigate(CustomerRoutes.AddRatingScreen.withPath(
                                        it.id
                                    ))
                                },
                                type = ButtonType.OUTLINED,
                                text = "Rate",
                                size = ButtonSize.SMALL,
                            )
                        }else if(it.isRated!=null && it.isRated){
                            CustomButton(
                                backgroundColor = Color.Gray,
                                onClick = {

                                },
                                type = ButtonType.OUTLINED,
                                text = "Rated",
                                size = ButtonSize.SMALL,
                            )
                        }
                    }
                )
            }
            if (!showMore.value && sellerOrder.orderItems?.size!! > 1) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            showMore.value = true
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show more", color = Color.Gray, fontSize = 14.sp)
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        "",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        Divider()
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                "Total price (${sellerOrder.orderItems?.size} item" +
                        "${if (sellerOrder.orderItems?.size!! > 1) "s" else ""}): ${
                            CurrencyConverter.toVND(
                                sellerOrder.finalPrice
                            )
                        }",
                fontSize = 15.sp, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth()
            )
        }

        if (sellerOrder.status == SELLER_ORDER_STATUS.PENDING) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                CustomButton(
                    text = "Cancel",
                    onClick = {
                        purchasedViewModel.userCancelSellerOrder(sellerOrder.id)
                    },
                    backgroundColor = colorResource(R.color.error_red),
                    type = ButtonType.OUTLINED,
                    size = ButtonSize.SMALL

                )
            }
        }
        if (sellerOrder.status == SELLER_ORDER_STATUS.CANCELLED) {
            Row (
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                CustomButton(
                    text = "By again",
                    onClick = {
                        navController?.navigate(CustomerRoutes.ProductDetailScreen.withPath(
                            sellerOrder.orderItems?.first()?.product?.id!!
                        ))
                    },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    size = ButtonSize.SMALL,
                    type = ButtonType.OUTLINED
                )
            }
        }
        if (sellerOrder.status == SELLER_ORDER_STATUS.DELIVERED) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, end = 8.dp, start = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Delivery note: Delivered",
                    fontSize = 15.sp,
                    color = colorResource(R.color.warning_orange),
                    modifier = Modifier.weight(1f)
                )
                CustomButton(
                    text = "Confirm",
                    onClick = {
                        purchasedViewModel.userConfirmSellerOrder(
                            sellerOrder.id
                        )
                    },
                    backgroundColor = colorResource(R.color.teal_700),
                    size = ButtonSize.SMALL,
                    type = ButtonType.OUTLINED
                )
            }
        }
    }
}

