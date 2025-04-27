package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.ProductsViewModel

@Composable
fun ProductDetailsScreen(
    viewModel: ProductsViewModel = hiltViewModel(),
    navController: NavController,
    productId: String
) {
}