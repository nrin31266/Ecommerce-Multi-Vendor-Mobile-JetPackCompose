package com.nrin31266.ecommercemultivendor.presentation.components.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.fununtils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.presentation.components.DiscountLabel

@Composable
fun ProductDetailsContent(
    product: ProductDto, isFavorite: Boolean = false,
) {
    val subProducts = product.subProducts
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                "${if (!subProducts.isNullOrEmpty()) subProducts.size else 1} variations available",
                color = Color.Gray, style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
        Box(
            modifier = Modifier.padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        CurrencyConverter.toVND(product.minSellingPrice),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.elegant_gold),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        CurrencyConverter.toVND(product.maxMrpPrice),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                    DiscountLabel(product.discountPercentage)
                }
            }

            Column(
                modifier = Modifier.align(Alignment.TopEnd),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Sold: ${product.totalSold}", style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (isFavorite) Icon(
                        imageVector = Icons.Default.Favorite, "", modifier = Modifier.size(20.dp),
                        tint = colorResource(R.color.error_red)
                    ) else
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            "",
                            modifier = Modifier.size(20.dp)
                        )
                }

            }
        }

        Row(
            modifier = Modifier.padding(horizontal = 8.dp)

        ) {
            Text(
                product.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }

}