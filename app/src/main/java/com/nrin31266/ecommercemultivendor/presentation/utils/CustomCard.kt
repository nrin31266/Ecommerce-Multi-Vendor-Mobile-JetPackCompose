package com.nrin31266.ecommercemultivendor.presentation.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(4.dp),
    backgroundColor: Color = Color.White,
    contentPaddingHorizontal: Int = 4,
    contentPaddingVertical: Int = 8,
    elevationDp: Int = 8,
    shadowDp: Int = 1,
    shadowColor: Color = Color.LightGray,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = contentPaddingHorizontal.dp, vertical = contentPaddingVertical.dp)
            .shadow(
                shadowDp.dp,
                spotColor = shadowColor
            )
            .clip(shape),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(elevationDp.dp)
    ) {
        content()
    }
}
