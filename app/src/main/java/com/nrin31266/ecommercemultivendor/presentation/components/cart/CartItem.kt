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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.funutils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.domain.dto.CartItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.request.AddUpdateCartItemRequest
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.CartViewModel


@Composable
fun CartItem(
    navController: NavController,
    cartItemDto: CartItemDto,
    cartViewModel: CartViewModel
) {
    val product = cartItemDto.product
    val subProduct = cartItemDto.subProduct!!

    val cartItemState = cartViewModel.cartItemState.collectAsStateWithLifecycle()
    val enable = cartItemState.value.mapEnable[cartItemDto.cartId.toString()] ?: true

    Box(
        modifier = Modifier.wrapContentSize()
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
                    .width(80.dp)
                    .height(90.dp)
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
                    Box(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                            .background(Color.LightGray.copy(0.1f))
                            .padding(horizontal = 4.dp)
                    ) {
                        val optionValues =
                            subProduct.options?.joinToString(", ") { it.optionValue ?: "" }
                        Text(
                            optionValues ?: "",
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)

                    ) {
                        Text(
                            CurrencyConverter.toVND(subProduct?.sellingPrice!!),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            CurrencyConverter.toVND(subProduct?.mrpPrice!!),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }


                }


            }
        }
        if (true) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        color = if (subProduct.quantity == 0) Color.Red.copy(0.3f)
                        else if ((cartItemDto.quantity!!) > (subProduct.quantity!!)) Color.LightGray.copy(
                            0.6f
                        )
                        else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable(enabled = false, onClick = {})
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            if (subProduct.quantity == 0 || ((cartItemDto.quantity!!) > (subProduct.quantity!!))) MaterialTheme.colorScheme.background else Color.Transparent,
                            RoundedCornerShape(
                                0.dp, 0.dp, 0.dp, 4.dp
                            )
                        )
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (subProduct.quantity == 0) {
                        Text(
                            "Out of stock",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.error_red)
                        )
                    } else
                        if ((cartItemDto.quantity!!) > (subProduct.quantity!!)) {
                            Text(
                                "Storage: ${subProduct.quantity}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }



                    if (!enable) {
                        CircularProgressIndicator(modifier = Modifier.size(12.dp))
                    } else {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            "",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(16.dp)
                                .clickable{
                                    cartViewModel.changeShowDialog(true, cartItemDto)
                                },
                            tint = colorResource(R.color.error_red)
                        )
                    }
                }
                if (subProduct.quantity != 0) {
                    Row(
                        modifier = Modifier
                            .padding(
                                bottom = 4.dp, end = 4.dp
                            )
                            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))

                            .padding(horizontal = 4.dp)
                            .align(Alignment.BottomEnd),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(18.dp)
                                .clickable(
                                    enabled = cartItemDto.quantity!! > 1
                                ) {
                                    cartViewModel.updateCartItem(cartItemDto.id!!, AddUpdateCartItemRequest(cartItemDto.quantity - 1))
                                },
                            tint = if (cartItemDto.quantity > 1) MaterialTheme.colorScheme.secondary else Color.LightGray
                        )
                        Text(
                            "${cartItemDto.quantity}",
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(18.dp)
                                .clickable(
                                    enabled = cartItemDto.quantity < subProduct.quantity!!
                                ) {
                                    cartViewModel.updateCartItem(cartItemDto.id!!, AddUpdateCartItemRequest(cartItemDto.quantity + 1))
                                },
                            tint = if (cartItemDto.quantity < subProduct.quantity!!) MaterialTheme.colorScheme.secondary else Color.LightGray
                        )
                    }
                }
            }
        }
    }
}