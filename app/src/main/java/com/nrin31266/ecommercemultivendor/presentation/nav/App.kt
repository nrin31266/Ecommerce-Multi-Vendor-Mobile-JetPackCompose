package com.nrin31266.ecommercemultivendor.presentation.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.nrin31266.ecommercemultivendor.common.AuthPreferences
import com.nrin31266.ecommercemultivendor.common.constant.USER_ROLE
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.HomeScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.LoginScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.AuthViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.SellerViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.UserViewModel
import com.nrin31266.ecommercemultivendor.presentation.seller.screen.SellerHomeScreen
import com.nrin31266.ecommercemultivendor.presentation.seller.screen.SellerLoginScreen
import javax.inject.Inject
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.example.bottombar.model.ItemStyle
import com.example.bottombar.model.VisibleItem
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.AccountScreen
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar

@Composable
fun App(
    authViewModel: AuthViewModel = hiltViewModel(),
//    sellerViewModel: SellerViewModel = hiltViewModel(),
//    userViewModel: UserViewModel = hiltViewModel(),

) {
    val context = LocalContext.current
    val navController = rememberNavController()

    // Create AuthPreferences manually if not using Hilt inject
    val authPreferences = remember { AuthPreferences(context) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val jwtState by authPreferences.jwtFlow.collectAsState(initial = null)
    val roleState by authPreferences.roleFlow.collectAsState(initial = null)


    var bottomBarKey by remember { mutableStateOf("customer") }
    LaunchedEffect(currentRoute) {
        bottomBarKey = when (currentRoute) {
            UserRoutes.UserHomeScreen.route , UserRoutes.UserAccountScreen.route -> "customer"
            SellerRoutes.SellerHomeScreen.route -> "seller"
            AdminRoutes.AdminHomeScreen.route -> "admin"
            else -> ""
        }
    }

    var startScreen by remember { mutableStateOf(UserRoutes.UserLoginScreen.route) }

    LaunchedEffect(jwtState) {
        if (jwtState != null) {
            when (roleState) {
                USER_ROLE.ROLE_SELLER.toString() -> {
                    startScreen = UserRoutes.UserHomeScreen.route
                }

                USER_ROLE.ROLE_CUSTOMER.toString() -> {
                    startScreen = UserRoutes.UserHomeScreen.route
                }

                USER_ROLE.ROLE_ADMIN.toString() -> {
                    startScreen = AdminRoutes.AdminHomeScreen.route
                }

                else -> {
                    startScreen = UserRoutes.UserLoginScreen.route
                }
            }
        } else {
            startScreen = UserRoutes.UserHomeScreen.route
        }
    }
    val userBottomItems = BottomNavItemsProvider.getUserBottomItems()
    val sellerBottomItems = BottomNavItemsProvider.getSellerBottomItems()
    val adminBottomItems = BottomNavItemsProvider.getAdminBottomItems()





    Scaffold(
        bottomBar = {
            when (bottomBarKey) {
                "customer" -> CustomAnimatedBottomBar(navController, currentRoute, userBottomItems)
                "seller" -> CustomAnimatedBottomBar(navController, currentRoute, sellerBottomItems)
                "admin" -> CustomAnimatedBottomBar(navController, currentRoute, adminBottomItems)
            }
        },
        modifier = Modifier
            .padding(

            ),

//        topBar = {
//            // Thêm TopAppBar trực tiếp không qua Scaffold
//            CustomTopBar(
//                title = "Nguyen Van",
//                actionIcon = Icons.Default.ArrowBack,
//
//
//                )
//        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController, startDestination = startScreen) {
                composable(UserRoutes.UserLoginScreen.route) {
                    LoginScreen(navController = navController)
                }
                composable(UserRoutes.UserHomeScreen.route) {
                    HomeScreen(navController)
                }

                composable(UserRoutes.UserAccountScreen.route){
                    AccountScreen(navController)
                }

                composable(SellerRoutes.SellerSignupScreen.route) {

                }


                composable(SellerRoutes.SellerLoginScreen.route) {
                    SellerLoginScreen(navController = navController)
                }
                composable(SellerRoutes.SellerHomeScreen.route) {
                    SellerHomeScreen(navController)
                }

//                navigation(
//                    route = SubNavigation.MainHomeAdminScreen.route,
//                    startDestination = AdminRoutes.AdminHomeScreen.route
//                ) {
//
//                }
            }
        }
    }

}

data class BottomNavItem(
    val name: String,
    val icon: ImageVector,
    val unSelectedIcon: ImageVector,
    val route: String
)

@Composable
fun CustomAnimatedBottomBar(
    navController: NavController,
    currentRoute: String?,
    bottomNavItems: List<BottomNavItem>
) {
    AnimatedBottomBar(
        selectedItem = bottomNavItems.indexOfFirst { it.route == currentRoute },
        itemSize = bottomNavItems.size,
        containerColor = Color.Transparent,
        indicatorColor = colorResource(id = R.color.elegant_gold),
        indicatorDirection = IndicatorDirection.BOTTOM,
        indicatorStyle = IndicatorStyle.WORM,
       modifier = Modifier.padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),

//        bottomBarHeight = 55.dp,

        indicatorHeight = 3.dp,




    ) {
        bottomNavItems.forEach { item ->
            BottomBarItem(
                selected = item.route == currentRoute,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(0) { inclusive = false } // or your actual base route
                            launchSingleTop = true
                        }
                    }
                },
                label = item.name,
                imageVector = if (item.route == currentRoute) item.icon else item.unSelectedIcon,
                containerColor = Color.Transparent,
                textColor = MaterialTheme.colorScheme.primary,

                itemStyle = ItemStyle.STYLE4,
                iconColor = if (item.route == currentRoute)
                    colorResource(id = R.color.elegant_gold)
                else colorResource(id = R.color.dark_slate),
                visibleItem = VisibleItem.ICON



            )
        }
    }
}
object BottomNavItemsProvider {

    fun getUserBottomItems() = listOf(
        BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home, UserRoutes.UserHomeScreen.route),
        BottomNavItem("Orders", Icons.Filled.List, Icons.Outlined.List, "user_orders"),
        BottomNavItem("Account", Icons.Filled.Person, Icons.Outlined.Person, UserRoutes.UserAccountScreen.route)

    )

    fun getSellerBottomItems() = listOf(
        BottomNavItem("Dashboard", Icons.Filled.Store, Icons.Outlined.Store, SellerRoutes.SellerHomeScreen.route),
        BottomNavItem("Products", Icons.Filled.List, Icons.Outlined.List, "seller_products"),
        BottomNavItem("Profile", Icons.Filled.Person, Icons.Outlined.Person, "seller_profile")
    )

    fun getAdminBottomItems() = listOf(
        BottomNavItem("Dashboard", Icons.Filled.Settings, Icons.Outlined.Settings, AdminRoutes.AdminHomeScreen.route),
        BottomNavItem("Users", Icons.Filled.SupervisorAccount, Icons.Outlined.SupervisorAccount, "admin_users"),
        BottomNavItem("Reports", Icons.Filled.BarChart, Icons.Outlined.BarChart, "admin_reports")
    )
}
