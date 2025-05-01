package com.nrin31266.ecommercemultivendor.presentation.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.nrin31266.ecommercemultivendor.common.fununtils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.domain.dto.CartItemDto


@Composable
fun CartItem(
    navController: NavController,
    cartItemDto: CartItemDto
) {
    val product = cartItemDto.product
    val subProduct = cartItemDto.subProduct

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        AsyncImage(
            model = cartItemDto.subProduct?.images?.firstOrNull(),
            contentDescription = null,
            modifier = Modifier.width(80.dp).height(90.dp).border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                .clip( RoundedCornerShape(4.dp))
        )
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(cartItemDto.product?.title ?:"",
                maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 14.sp, fontWeight = FontWeight.SemiBold
            )

            if ( product != null&& !product.isSubProduct) {
                Box(
                    modifier = Modifier.padding(bottom = 8.dp).border(1.dp, Color.LightGray, RoundedCornerShape(4.dp)).background(Color.LightGray.copy(0.1f))
                        .padding(horizontal = 4.dp)
                ){
                    val optionValues = subProduct?.options?.map { it.optionValue ?: "" }?.joinToString(", ")
                    Text(optionValues ?: "", fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,)
                }
            }
            Row (
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ){
                Row (
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)

                ){
                    Text(CurrencyConverter.toVND(subProduct?.sellingPrice!!), fontSize = 14.sp, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary)
                    Text(CurrencyConverter.toVND(subProduct?.mrpPrice!!), fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary, textDecoration = TextDecoration.LineThrough)
                }

                Row (
                    modifier = Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))

                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp).clickable {

                        },
                    )
                    Text("${cartItemDto.quantity}", fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp).clickable {

                        }
                    )
                }
            }


        }
    }
}