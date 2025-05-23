package com.nrin31266.ecommercemultivendor.presentation.nav


import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.example.bottombar.model.ItemStyle
import com.example.bottombar.model.VisibleItem
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.AuthPreferences
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.AccountScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.AddEditAddressScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.AddRatingScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.AddressScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.CartScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.CheckoutScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.HomeScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.LoginScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.NotificationsScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.OrdersScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.PaymentScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.ProductDetailsScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.ProductsScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.ReviewScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.SearchScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.SellerOrderDetailsScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.ShopScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.SignupScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.WishlistScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.purchased_screen.PurchasedScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.AuthViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.CartViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.HomeViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.ProfileViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.SharedAddressViewModel

@Composable
fun App() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val cartViewModel: CartViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val sharedAddressViewModel: SharedAddressViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()

    val context = LocalContext.current
    val navController = rememberNavController()
    // Create AuthPreferences manually if not using Hilt inject
    val authPreferences = remember { AuthPreferences(context) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isShowBottomBar = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("vnpay", Context.MODE_PRIVATE)
        val hasPendingResult = prefs.getBoolean("payment_result_pending", false)

        if (hasPendingResult) {
            val success = prefs.getBoolean("payment_success", false)
            prefs.edit().clear().apply()

            navController.navigate(CustomerRoutes.PurchasedScreen.withPath(if (success) 1 else 0))
        }
    }

    LaunchedEffect(currentRoute) {
        isShowBottomBar.value = when (currentRoute) {
            CustomerRoutes.CustomerHomeScreen.route, CustomerRoutes.CustomerAccountScreen.route, CustomerRoutes.WishlistScreen.route ,
                CustomerRoutes.NotificationsScreen.route-> true
            else -> false
        }
    }

    val jwtState by authPreferences.jwtFlow.collectAsState(initial = null)


    val startScreen by remember { mutableStateOf(SubNavigation.MainCustomerScreen.route) }

    LaunchedEffect(jwtState) {
        if (jwtState != null) {
            Log.d("App", "Logged in with JWT: $jwtState");

        } else {
            Log.d("App", "Not logged in");
        }
    }


    val isLoggedIn = authViewModel.userAuthState.collectAsStateWithLifecycle().value.isLogin
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            // Reset profile
            Log.d("App", "Load user profile");
            profileViewModel.getUserProfile()
            // Reset cart
            Log.d("App", "Load user cart");
            cartViewModel.getUserCart()

        } else {
            Log.d("App", "Remove user cart");
            cartViewModel.clearCart()
            Log.d("App", "Remove user profile");
        }
    }

//    var lastJwt by remember { mutableStateOf<String?>(null) }
//
//    LaunchedEffect(authViewModel.userAuthState.value.jwt) {
//        val currentJwt = authViewModel.userAuthState.value.jwt
//        if (!currentJwt.isNullOrBlank() && currentJwt != lastJwt) {
//            lastJwt = currentJwt
//            cartViewModel.loadCart()
//        }
//    }


    Scaffold(
        bottomBar = {
            if (isShowBottomBar.value) {
                BottomBarNav(navController)
            }
        },
        modifier = Modifier
            .padding(

            ),

        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController, startDestination = startScreen) {

                //  User NavGraph
                navigation(
                    route = SubNavigation.MainCustomerScreen.route,
                    startDestination = CustomerRoutes.CustomerHomeScreen.route
                ) {

                    composable(CustomerRoutes.CustomerHomeScreen.route) {
                        HomeScreen(navController, authViewModel, cartViewModel, homeViewModel)
                    }
                    composable(CustomerRoutes.CustomerAccountScreen.route) {
                        AccountScreen(navController, authViewModel, profileViewModel, cartViewModel)
                    }
                    composable(CustomerRoutes.CustomerOrdersScreen.route) {
                        OrdersScreen(navController)
                    }
                    composable(CustomerRoutes.SearchScreen.route) {
                        SearchScreen(navController)
                    }
                    composable(
                        route = "${CustomerRoutes.ProductsScreen.route}?search={search}&category={category}&sort={sort}",
                        arguments = listOf(
                            navArgument("search") { defaultValue = "" },
                            navArgument("category") { defaultValue = "" },
                            navArgument("sort") { defaultValue = "" }
                        )
                    ) { backStackEntry ->
                        val search = backStackEntry.arguments?.getString("search") ?: ""
                        val category = backStackEntry.arguments?.getString("category") ?: ""
                        val sort = backStackEntry.arguments?.getString("sort") ?: ""

                        ProductsScreen(
                            search = search,
                            category = category,
                            sort = sort,
                            navController = navController
                        )
                    }


                    composable(
                        route = CustomerRoutes.ProductDetailScreen.route,
                        arguments = listOf(
                            navArgument("productId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")
                        ProductDetailsScreen(
                            productId = productId!!,
                            navController = navController,
                            authViewModel = authViewModel,
                            cartViewModel = cartViewModel
                        )
                    }

                    composable(CustomerRoutes.CustomerSignupScreen.route) {
                        SignupScreen(navController)
                    }
                    composable(
                        route = CustomerRoutes.CustomerLoginScreen.route,
                        arguments = listOf(
                            navArgument("redirect") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            }
                        )
                    ) { backStackEntry ->
                        val redirect = backStackEntry.arguments?.getString("redirect")

                        LoginScreen(
                            navController = navController,
                            redirect = redirect,
                            authViewModel = authViewModel
                        )
                    }
                    composable(
                        route = CustomerRoutes.CartScreen.route
                    ) {
                        CartScreen(navController, cartViewModel)
                    }
                    composable(
                        route = CustomerRoutes.CheckoutScreen.route
                    ) {
                        CheckoutScreen(
                            navController,
                            cartViewModel,
                            sharedAddressViewModel = sharedAddressViewModel
                        )
                    }
                    composable(
                        route = CustomerRoutes.AddressScreen.route
                    ) {

                        AddressScreen(
                            navController,
                            sharedAddressViewModel = sharedAddressViewModel
                        )
                    }
                    composable(
                        route = CustomerRoutes.AddEditAddressScreen.route,
                        arguments = listOf(
                            navArgument("addressId") {
                                type = NavType.LongType
                                nullable = false
                                defaultValue = -1
                            }
                        )
                    ) {
                        val addressId = it.arguments?.getLong("addressId") ?: -1
                        AddEditAddressScreen(navController, addressId)
                    }
                    composable(
                        route = CustomerRoutes.PurchasedScreen.route,
                        arguments = listOf(
                            navArgument("tabIndex") {
                                type = NavType.IntType
                                defaultValue = 0
                            }
                        )
                    ) {
                        val tabIndex = it.arguments?.getInt("tabIndex") ?: 0
                        PurchasedScreen(navController, tabIndex)
                    }
                    composable(
                        route = CustomerRoutes.SellerOrderDetailsScreen.route,
                        arguments = listOf(
                            navArgument("sellerOrderId") {
                                type = NavType.LongType
                                defaultValue = -1
                            }
                        )
                    ) {
                        val sellerOrderId = it.arguments?.getLong("sellerOrderId") ?: -1
                        SellerOrderDetailsScreen(navController, sellerOrderId)
                    }
                    composable(
                        route = CustomerRoutes.AddRatingScreen.route,
                        arguments = listOf(
                            navArgument("orderId") {
                                type = NavType.LongType
                                defaultValue = -1
                            }
                        )
                    ) {
                        val orderId = it.arguments?.getLong("orderId") ?: -1
                        AddRatingScreen(
                            navController,
                            orderId
                        )
                    }
                    composable(CustomerRoutes.WishlistScreen.route) {
                        WishlistScreen(
                            navController,
                            authViewModel = authViewModel
                        )
                    }
                    composable(CustomerRoutes.NotificationsScreen.route) {
                        NotificationsScreen(
                            navController
                        )
                    }
                    composable(
                        route = CustomerRoutes.PaymentWebViewScreen.route,
                        arguments = listOf(navArgument("url") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val url = backStackEntry.arguments?.getString("url")
                        if (url != null) {
                            PaymentScreen(url, navController = navController)
                        }
                    }
                    composable(
                        route= CustomerRoutes.RatingScreen.route,
                        arguments = listOf(
                            navArgument("productId") { type = NavType.LongType }
                        )
                    ){
                        backStackEntry ->
                        val productId = backStackEntry.arguments?.getLong("productId") ?: -1
                        ReviewScreen(navController, productId = productId)
                    }
                    composable(
                        route = CustomerRoutes.ShopScreen.route,
                        arguments = listOf(
                            navArgument("shopId") { type = NavType.LongType }
                        )
                    ){
                        backStackEntry ->
                        val shopId = backStackEntry.arguments?.getLong("shopId") ?: -1
                        ShopScreen(navController, shopId = shopId)

                    }


                }


            }

        }
    }

}

