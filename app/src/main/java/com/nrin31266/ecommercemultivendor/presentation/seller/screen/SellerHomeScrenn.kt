package com.nrin31266.ecommercemultivendor.presentation.seller.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.AuthViewModel

@Composable
fun SellerHomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    Text("SellerHomeScreen")

}