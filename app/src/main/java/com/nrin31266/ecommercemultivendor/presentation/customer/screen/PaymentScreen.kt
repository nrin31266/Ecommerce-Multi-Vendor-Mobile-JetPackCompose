package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@Composable
fun PaymentScreen(vnpayUrl: String, navController: NavController) {
    val context = LocalContext.current

    LaunchedEffect (Unit) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(vnpayUrl))
        context.startActivity(intent)

        // Quay về hoặc hiển thị chờ xử lý
        navController.popBackStack()
        // Hoặc điều hướng sang màn hình "đợi xác nhận"
        // navController.navigate(CustomerRoutes.WaitingPaymentResult)
    }
}

