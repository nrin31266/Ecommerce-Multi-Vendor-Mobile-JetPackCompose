package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.AuthViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState = authViewModel.userAuthState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
//            CustomTopBar(
//                title = "Nguyen Van",
//                modifier = Modifier,
//                onBackClick = {
//
//                }
//
//            )
        },
        contentWindowInsets = WindowInsets(0),

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(

                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(R.color.elegant_gold))
                    .padding(
                        top = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding()
                    )
                    .padding(
                        bottom = 16.dp
                    )
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton({}) {
                        Icon(Icons.Default.Settings, "")
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                   if(authState.value.isLogin){
                       Button(
                           {
                              authViewModel.logout()
                           }, modifier = Modifier, shape = RoundedCornerShape(8.dp)
                       ) {
                           Text("Logout")
                       }
                   }else{
                       Button(
                           {
                               navController.navigate(CustomerRoutes.CustomerLoginScreen.route)
                               {
//                                popUpTo(UserRoutes.UserHomeScreen.route) { inclusive = true }
//                                launchSingleTop = true
                               }
                           }, modifier = Modifier, shape = RoundedCornerShape(8.dp)
                       ) {
                           Text("Login")
                       }
                   }

                }
            }
        }
    }


}