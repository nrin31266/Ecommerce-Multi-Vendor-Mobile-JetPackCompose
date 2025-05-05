package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoveToInbox
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.AuthViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomCard
import com.nrin31266.ecommercemultivendor.presentation.utils.HeaderSection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    authViewModel: AuthViewModel,

) {
    val authState = authViewModel.userAuthState.collectAsStateWithLifecycle()
    Scaffold(

        contentWindowInsets = WindowInsets(0),

        ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                item {
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
                                if (authState.value.isLogin) {
                                    Button(
                                        {
                                            authViewModel.logout()
                                        }, modifier = Modifier, shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Logout")
                                    }
                                } else {
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
                item {
                    CustomCard(
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            HeaderSection(
                                title = "Purchase order",
                                actionName = "Purchase history",
                                onActionClick = {}
                            )
                            PurchaseContent(navController)
                        }
                    }
                }
            }
        }
    }
}

data class PurchaseItem(
    val title: String,
    val notificationCount: Int = 0,
    val icon: ImageVector,
    val onClick: () -> Unit = {}
)

@Composable
fun PurchaseItemComponent(
    item: PurchaseItem,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.height(100.dp).clickable {
            item.onClick()
        } // hoặc chiều cao bạn cần
    ) {
        Icon(item.icon, contentDescription = "")

        Spacer(modifier = Modifier.height(6.dp)) // khoảng cách mềm mại

        Text(
            text = item.title,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center, // căn giữa văn bản trong chính nó
            modifier = Modifier
        )
    }


}

@Composable
fun PurchaseContent(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val render = listOf(
            PurchaseItem("To pay", 0, Icons.Default.Payments, {
                navController.navigate(CustomerRoutes.PurchasedScreen.withPath(0))
            }),
            PurchaseItem("To confirm", 0, Icons.Default.Inventory, {
                navController.navigate(CustomerRoutes.PurchasedScreen.withPath(1))
            }),
            PurchaseItem("To pickup", 0, Icons.Default.MoveToInbox, {
                navController.navigate(CustomerRoutes.PurchasedScreen.withPath(2))
            }),
            PurchaseItem("Shipping", 0, Icons.Default.LocalShipping, {
                navController.navigate(CustomerRoutes.PurchasedScreen.withPath(3))
            }),
            PurchaseItem("To rate", 0, Icons.Default.Stars,{

            })
        )
        render.map {
            PurchaseItemComponent(item = it)
        }
    }
}