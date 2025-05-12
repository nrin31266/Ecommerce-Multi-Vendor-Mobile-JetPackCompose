package com.nrin31266.ecommercemultivendor.presentation.components.purchased

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.funutils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.domain.dto.PaymentDto
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.PurchasedViewModel
import com.nrin31266.ecommercemultivendor.presentation.utils.ButtonSize
import com.nrin31266.ecommercemultivendor.presentation.utils.ButtonType
import com.nrin31266.ecommercemultivendor.presentation.utils.CountdownTimer
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomButton
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomCard

@Composable
fun PendingPaymentItem(
    paymentDto: PaymentDto,
    navController: NavController,
    purchasedViewModel: PurchasedViewModel,
) {

    CustomCard(
        elevationDp = 0,
        shadowDp = 0
        , shadowColor = Color.White
    ) {
        Column {
            Row (
                modifier = Modifier.padding(horizontal = 8.dp)
            ){
                Text("Payment now", modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp)
                CountdownTimer(paymentDto.expiryDate)
            }
            Column (
                modifier = Modifier.padding(horizontal = 8.dp)
            ){
                paymentDto.order.sellerOrders.map {
                    SellerOrderCardItem(it, purchasedViewModel,navController)
                }
            }
            Divider()
            Column (
                modifier = Modifier.padding(8.dp)
            ){
                Text("Amount: ${CurrencyConverter.toVND(paymentDto.amount)}", overflow = TextOverflow.Ellipsis, maxLines = 1,
                    fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Method: ${paymentDto.paymentMethod}", overflow = TextOverflow.Ellipsis, maxLines = 1,
                        fontSize = 14.sp, color = colorResource(R.color.warning_orange,), modifier = Modifier.weight(1f)
                    )
                    Row (
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),

                    ){
                        CustomButton(size = ButtonSize.SMALL, text = "Cancel", onClick = {
                            purchasedViewModel.userCancelPayment(paymentDto.id)
                        },
                            backgroundColor = colorResource(R.color.error_red),
                            type = ButtonType.OUTLINED
                        )
                        CustomButton(size = ButtonSize.SMALL, text = "Payment", onClick = {},
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            type = ButtonType.OUTLINED
                        )
                    }
                }

            }
        }
    }
}