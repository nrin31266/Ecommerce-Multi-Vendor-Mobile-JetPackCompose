package com.nrin31266.ecommercemultivendor.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.constant.ACCOUNT_STATUS
import com.nrin31266.ecommercemultivendor.common.constant.USER_ROLE
import com.nrin31266.ecommercemultivendor.common.funutils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.domain.dto.AddressDto
import com.nrin31266.ecommercemultivendor.domain.dto.BankDetailsDto
import com.nrin31266.ecommercemultivendor.domain.dto.BusinessDetailsDto
import com.nrin31266.ecommercemultivendor.domain.dto.CategoryDto
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.ProductOptionTypeDto
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto



@Composable

fun ProductItem(
    onClick: () -> Unit ={},
    item: ProductDto,
    modifier: Modifier = Modifier

) {
    Card(
        modifier = modifier
            .padding(0.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column  {
            // Hình ảnh sản phẩm
            AsyncImage(
                model = item.images.firstOrNull(),
                contentDescription = item.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth().background(Color.White)
            )

            Column(modifier = Modifier.padding(6.dp)) {
                // Tiêu đề
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.seller?.businessDetails?.businessName?:"",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))


                Row (verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = CurrencyConverter.toVND(item.minSellingPrice) ,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    DiscountLabel(item.discountPercentage)

                }
                Spacer(modifier = Modifier.height(2.dp))

                // Đánh giá và đã bán
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier
                        .background(
                            color = colorResource(R.color.elegant_gold).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 2.dp, vertical = 2.dp)){
                       Row{
                           Icon(
                               imageVector = Icons.Default.Star,
                               contentDescription = null,
                               tint = colorResource(R.color.elegant_gold),
                               modifier = Modifier.size(16.dp)
                           )
                           Spacer(modifier = Modifier.width(2.dp))
                           Text(
                               text = "${item.avgRating}",
                               style = MaterialTheme.typography.bodySmall
                           )
                       }
                    }
                    Text("|",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        color = Color.Gray)

                    Text(
                        text = "Sold ${item.totalSold}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
