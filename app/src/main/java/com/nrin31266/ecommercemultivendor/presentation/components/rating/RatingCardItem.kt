package com.nrin31266.ecommercemultivendor.presentation.components.rating

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.funutils.DateUtils
import com.nrin31266.ecommercemultivendor.domain.dto.ReviewDto
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomCard
import java.util.Date

@Composable
fun RatingCardItem(
   review: ReviewDto
) {
    val product = review.product
    val options = review.subProduct.options
    val author = review.user

    CustomCard (
        backgroundColor = colorResource(R.color.light_background),
    ){
        Column (
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Row {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ){
                    AsyncImage(
                        model = R.drawable.avatar,
                        "",
                        modifier = Modifier.size(24.dp).clip(
                            CircleShape
                        ).border(1.dp, Color.LightGray, CircleShape),
                    )
                    Text("${author.fullName}", fontSize = 12.sp)
                }
                Text(DateUtils.timeAgo(review.createdAt),
                    fontSize = 12.sp, color = Color.Gray)
            }
            Row (
                modifier = Modifier.padding(top = 4.dp)
            ){
                for (i in 1..review.reviewRating) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier
                            .size(12.dp),
                        tint = Color(0xFFFFC107)
                    )
                }
            }
            if(!options.isNullOrEmpty()){
                val variant = options.joinToString(", ") {
                    "${it.optionType?.value}: ${it.optionValue}"
                }
                Text("Variant: $variant",
                    maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.Gray,
                    fontSize = 13.sp
                )
            }
            if(review.reviewText.isNotEmpty()){
                Text(review.reviewText, fontSize = 13.sp)
            }
            if(review.reviewImages.isNotEmpty()){
                Row (
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ){
                    review.reviewImages.forEach {
                        AsyncImage(
                            model = it,
                            "",
                            modifier = Modifier.size(60.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}