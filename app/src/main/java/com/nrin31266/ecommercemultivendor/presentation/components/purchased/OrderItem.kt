package com.nrin31266.ecommercemultivendor.presentation.components.purchased

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import coil.compose.AsyncImage
import com.nrin31266.ecommercemultivendor.common.fununtils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.domain.dto.OrderItemDto

@Composable
fun OrderItem(
    orderItem: OrderItemDto
) {
    val product = orderItem.product
    val subProduct = orderItem.subProduct

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AsyncImage(
            model = orderItem.subProduct?.images?.firstOrNull(),
            contentDescription = null,
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp))
        )
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                orderItem.product?.title ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            if (!product.isSubProduct) {
                val optionValues =
                    subProduct.options?.joinToString(", ") { it.optionValue ?: "" }
                Text(
                    optionValues ?: "",
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier

            ) {
                Text(
                    CurrencyConverter.toVND(orderItem?.sellingPrice!!),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    CurrencyConverter.toVND(orderItem?.mrpPrice!!),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary,
                    textDecoration = TextDecoration.LineThrough
                )
            }
            Text("x${orderItem.quantity}",
                fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.secondary)


        }
    }
}