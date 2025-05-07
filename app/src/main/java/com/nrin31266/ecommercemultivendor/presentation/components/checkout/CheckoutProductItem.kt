package com.nrin31266.ecommercemultivendor.presentation.components.checkout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nrin31266.ecommercemultivendor.common.funutils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.domain.dto.CartItemDto


@Composable
fun CheckoutProductItem(
    navController: NavController,
    cartItemDto: CartItemDto,
) {
    val product = cartItemDto.product
    val subProduct = cartItemDto.subProduct!!

    Box(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = cartItemDto.subProduct?.images?.firstOrNull(),
                contentDescription = null,
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(4.dp))
            )
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    cartItemDto.product?.title ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (product != null && !product.isSubProduct) {
                    val optionValues =
                        subProduct.options?.joinToString(", ") { it.optionValue ?: "" }
                    Text(
                        optionValues ?: "",
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)

                    ) {
                        Text(
                            CurrencyConverter.toVND(subProduct.sellingPrice!!),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            CurrencyConverter.toVND(subProduct.mrpPrice!!),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                    Text("x${cartItemDto.quantity}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.secondary)
                }


            }
        }

    }
}