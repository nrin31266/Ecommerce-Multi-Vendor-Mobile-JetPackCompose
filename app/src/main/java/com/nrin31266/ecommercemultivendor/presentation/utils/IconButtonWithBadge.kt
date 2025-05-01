package com.nrin31266.ecommercemultivendor.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IconButtonWithBadge(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector = Icons.Default.ShoppingCart,
    badgeCount: Int = 10000,
    contentDescription: String? = null,
) {
    Box(
    ) {
        // IconButton
        IconButton (onClick = { onClick() },
            modifier = modifier) {

            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
            )
        }

        // Badge (Số tròn nhỏ)
        if(badgeCount > 0) {
            val badgeText = if (badgeCount > 99) "99+" else badgeCount.toString()
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd) // Đặt badge ở góc trên bên phải
                    .size(20.dp)
                    .border(1.dp, MaterialTheme.colorScheme.background, shape = CircleShape)// Kích thước của badge
                    .background(Color.Red, shape = CircleShape) // Màu nền và hình dạng tròn
                    .padding(2.dp) // Đệm bên trong để text không chạm viền

            ) {
                Text(
                    text = badgeText, // Số badge
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = if(badgeCount > 99) 9.sp else 11.sp),
                    modifier = Modifier.align(Alignment.Center), // Canh giữa số trong vòng tròn
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}
