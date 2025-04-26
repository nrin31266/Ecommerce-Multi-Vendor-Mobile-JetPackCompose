package com.nrin31266.ecommercemultivendor.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TightIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
//    backgroundColor: Color = Color.LightGray,
    shape: Shape = CircleShape,
    icon: ImageVector,
    contentDescription: String? = "",
    iconSize: Dp = 24.dp,
    badgeCount: Int = 0
) {
    Box(
        modifier = modifier
            .size(iconSize)
//            .background(backgroundColor, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize)
        )

        if (badgeCount > 0) {
            // Sử dụng offset để điều chỉnh badge sát góc trên bên phải
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .offset(x = (iconSize / 2) - 2.dp, y = (-14).dp)
                    .background(Color.Red, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badgeCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold
                )
            }
        }
    }
}