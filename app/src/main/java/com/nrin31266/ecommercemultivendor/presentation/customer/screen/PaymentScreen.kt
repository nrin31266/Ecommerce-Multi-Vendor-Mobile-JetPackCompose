package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading

@Composable
fun PaymentScreen(linkPayment: String, navController: NavController) {
    val context = LocalContext.current

    LaunchedEffect (Unit) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkPayment))
        context.startActivity(intent)
//
//        // Quay về hoặc hiển thị chờ xử lý
//        navController.popBackStack()
//        // Hoặc điều hướng sang màn hình "đợi xác nhận"
//        // navController.navigate(CustomerRoutes.WaitingPaymentResult)
    }

    Scaffold {
            innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentAlignment = Alignment.Center){
           FullScreenLoading("Waiting payment")
        }
    }
}

