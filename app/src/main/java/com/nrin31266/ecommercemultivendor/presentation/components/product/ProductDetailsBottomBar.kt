package com.nrin31266.ecommercemultivendor.presentation.components.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto

@Composable
fun ProductDetailsBottomBar(product: ProductDto, onAddToCartClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .padding(
                bottom = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding()
            )
            .background(Color.LightGray)
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .background(colorResource(R.color.teal_700))
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Default.Chat, "", tint = Color.White)
            Text("Chat now", color = Color.White, style = MaterialTheme.typography.bodySmall)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize()
                .clickable { onAddToCartClick() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(imageVector = Icons.Default.ShoppingCart, "", tint = Color.White)
            Text("Add to cart", color = Color.White, style = MaterialTheme.typography.bodySmall)

        }
    }
}