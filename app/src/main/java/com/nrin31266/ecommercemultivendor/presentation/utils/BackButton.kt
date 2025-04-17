package com.nrin31266.ecommercemultivendor.presentation.utils

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@Composable
fun BackButton(
    navController: NavController,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.ArrowBack,
    contentDescription: String = "Back"
) {
    IconButton (
        onClick = { navController.popBackStack() },
        modifier = modifier
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}
