package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun NotificationsScreen(navController: NavController) {
    Scaffold {innerPadding->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text("Not notifications")
            }
        }
    }
}