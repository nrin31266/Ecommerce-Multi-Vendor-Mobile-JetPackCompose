package com.nrin31266.ecommercemultivendor.presentation.components



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun DiscountLabel(
    discountPercentage: Int, // Giảm giá
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.error, // Màu chữ (mặc định là đỏ)
    backgroundColor: Color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f), // Màu nền (mặc định là đỏ nhạt)
    cornerRadius: Dp = 4.dp, // Bo góc
    fontSize: TextUnit = 10.sp, // Cỡ chữ
    horizontalPadding: Dp = 4.dp, // Padding ngang
    verticalPadding: Dp = 0.dp // Padding dọc
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),

    ) {
        Text(
            text = "-$discountPercentage%", // Hiển thị giá trị giảm giá
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = textColor,
            maxLines = 1,

        )
    }
}
