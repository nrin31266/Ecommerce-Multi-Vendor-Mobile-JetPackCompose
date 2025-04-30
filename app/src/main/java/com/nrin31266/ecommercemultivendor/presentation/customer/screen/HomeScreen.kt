package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.AuthViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.SearchBar


@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
) {

    val authState = authViewModel.userAuthState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
           CustomTopBar(content = {
               Row (modifier = Modifier, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                   SearchBar({
                       navController.navigate(CustomerRoutes.SearchScreen.route) {
                           popUpTo(CustomerRoutes.CustomerHomeScreen.route) { inclusive = false }
                           launchSingleTop = true
                       }
                   }, modifier = Modifier.weight(1f))
               }
           },
               actionIcon = Icons.Default.ShoppingCart,
               onActionClick = {}
           )
        },
        contentWindowInsets = WindowInsets(0),

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {

            Text(authState.value.jwt?:"Not logged in")
        }
    }

}