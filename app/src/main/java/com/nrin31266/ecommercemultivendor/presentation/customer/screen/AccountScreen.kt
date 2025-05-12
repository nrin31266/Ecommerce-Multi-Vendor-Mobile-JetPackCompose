package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MoveToInbox
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.AuthViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.CartViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.ProfileViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomCard
import com.nrin31266.ecommercemultivendor.presentation.utils.HeaderSection


@Composable
fun AccountScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    cartViewModel: CartViewModel
) {
    val authState = authViewModel.userAuthState.collectAsStateWithLifecycle()
    val profileState = profileViewModel.state.collectAsStateWithLifecycle()
    val cartInfoState = cartViewModel.cartInfoState.collectAsStateWithLifecycle()
    Scaffold(

        contentWindowInsets = WindowInsets(0),

        ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                item {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),

                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colorResource(R.color.midnight_blue).copy(0.6f))
                                .padding(
                                    top = WindowInsets.statusBars
                                        .asPaddingValues()
                                        .calculateTopPadding()
                                )
                                .padding(
                                    horizontal = 8.dp,
                                    vertical = 12.dp
                                ),
                            verticalAlignment = Alignment.Bottom,
                        ) {
                            Column(
                                modifier = Modifier.weight(1f).fillMaxSize(),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                if(authState.value.isLogin && profileState.value.userDto != null){
                                    ProfileSection(profileViewModel)
                                }else{
                                    Text("Login to explore more", fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
                                        color = Color.White
                                    )
                                }
                            }
                            Column {

                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .align(Alignment.End),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton({}) {
                                        Icon(Icons.Default.Settings, "st", tint = Color.White)
                                    }
                                    BadgedBox(modifier = Modifier.clickable {
                                        if(authState.value.isLogin){
                                            navController.navigate(CustomerRoutes.CartScreen.route)
                                        }else{
                                            navController.navigate(CustomerRoutes.CustomerLoginScreen.route)
                                        }
                                    }.padding(8.dp),badge = {
                                        if (cartInfoState.value.totalCartItem>0) {
                                            Badge{
                                                Text(cartInfoState.value.totalCartItem.toString())
                                            }
                                        }
                                    }) {
                                        Icon(Icons.Default.ShoppingCart, "Spc", tint = Color.White)
                                    }


                                }
                                Row(
                                    modifier = Modifier
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
                                onActionClick = {
                                    navController.navigate(CustomerRoutes.PurchasedScreen.withPath(4))
                                }
                            )
                            PurchaseContent(navController, modifier = Modifier.padding(vertical = 16.dp), authViewModel)
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun ProfileSection(profileViewModel: ProfileViewModel){
    val profileState = profileViewModel.state.collectAsStateWithLifecycle()
    val user = profileState.value.userDto
   Row(
       verticalAlignment = Alignment.Bottom
   ) {
       AsyncImage(
           model = R.drawable.avatar,
           contentScale = ContentScale.Crop,
           modifier = Modifier.border(1.dp, Color.Gray, CircleShape)
               .background(Color.White, CircleShape).clip(CircleShape)
               .size(100.dp),
           contentDescription = "avt",

       )

        Text("Hello ${user?.fullName}", fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis,
            color = Color.White
        )


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
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable {
            onClick()
        }.clip(RoundedCornerShape(8.dp)),
    ) {
        Icon(item.icon, contentDescription = "", modifier = Modifier.size(28.dp))

        Spacer(modifier = Modifier.height(6.dp))

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
fun PurchaseContent(navController: NavController, modifier: Modifier=Modifier, authViewModel: AuthViewModel) {
    val authState = authViewModel.userAuthState.collectAsStateWithLifecycle()
    Row(
        modifier = modifier.fillMaxWidth(),
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
            PurchaseItemComponent(item = it, {
                if(authState.value.isLogin){
                    it.onClick()
                }else{
                    navController.navigate(CustomerRoutes.CustomerLoginScreen.route)
                }
            })
        }
    }
}