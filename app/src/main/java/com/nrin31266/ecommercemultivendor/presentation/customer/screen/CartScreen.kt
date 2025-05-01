package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.CartViewModel

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
) {

    val cartInfoState = cartViewModel.cartInfoState.collectAsStateWithLifecycle()
    Scaffold {
        innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(text = "Total item in cart: ${cartInfoState.value.totalItem}")
        }
    }
}