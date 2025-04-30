package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.common.constant.USER_ROLE
import com.nrin31266.ecommercemultivendor.domain.dto.request.AuthRequest
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.AuthEvent
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.AuthViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.ButtonType
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomButton
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomOTPField
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTextField
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType
import com.nrin31266.ecommercemultivendor.presentation.utils.TextDivider


@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    redirect: String?
) {
    val state = authViewModel.userAuthState.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect (Unit){
        // Log các màn hình hiện tại trong back stack
        navController.currentBackStackEntry?.destination?.route?.let {
            Log.d("NavController", "Current route: $it")
        }



        state.value.let {
            email = it.currentEmail ?: ""
        }
        authViewModel.authEvent.collect { event ->
            when (event) {
                is AuthEvent.NavigateToHome -> {
                    navController.navigate(redirect ?: CustomerRoutes.CustomerHomeScreen.route) {


                        popUpTo(CustomerRoutes.CustomerLoginScreen.route) { inclusive = true }
                        popUpTo(CustomerRoutes.ProductDetailScreen.route) { inclusive = true }
                    }


                }
                is AuthEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Login",
                actionIcon = Icons.Default.Info,
                modifier = Modifier,
                onBackClick = {
                    navController.popBackStack()
                },
                onActionClick = {

                }

            )

        },

        ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            Column (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ){
                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    placeholder = "Enter your email",
                    leadingIcon = Icons.Default.Email,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)

                )
                 if(state.value.isSentOtp){
                     CustomOTPField(
                         value = otp,
                         onValueChange = { otp = it },
                         canResend = state.value.timeLeft == 0,
                         timeRemaining = state.value.timeLeft,
                         onResend = {
                             val request = AuthRequest(
                                 email = email,
                                 role = USER_ROLE.ROLE_CUSTOMER
                             )
                             authViewModel.sendEmailOtp(request, true)
                         }
                     )
                 }

                Spacer(Modifier.size(8.dp))

                state.value.errorMessage?.let {
                    if (it.isNotEmpty()){
                        CustomMessageBox(
                            message = it,
                            type = MessageType.ERROR,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }


                Spacer(Modifier.size(16.dp))

                CustomButton(
                    text = if(!state.value.isSentOtp) "Sent OTP" else "Login",
                    onClick = {



                        if(!state.value.isSentOtp){
                            val request = AuthRequest(
                                email = email,
                                role = USER_ROLE.ROLE_CUSTOMER
                            )
                            authViewModel.sendEmailOtp(request, true)
                        }else{
                            val request = AuthRequest(
                                email = email,
                                otp = otp,
                                role = USER_ROLE.ROLE_CUSTOMER
                            )
                            authViewModel.userLogin(request,
                            )
                        }

                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    enabled = !state.value.isLoading,
                    loading = state.value.isLoading
                )
                Spacer(Modifier.size(12.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("If you don't have an account")
                    CustomButton(
                        text = "Register",
                        onClick = {
                            navController.navigate(CustomerRoutes.CustomerSignupScreen.route)
//                            {
//                                popUpTo(CustomerRoutes.CustomerLoginScreen.route) { inclusive = true }
////                                launchSingleTop = true
//                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        type = ButtonType.OUTLINED
                    )
                }
                Spacer(Modifier.size(16.dp))

                TextDivider(
                    text = "Or",
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                CustomButton(
                    text = "Become Seller",
                    onClick = {
//                        navController.navigate("register")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    type = ButtonType.OUTLINED,
                    icon = Icons.Default.Storefront
                )


            }

        }
    }


}