package com.nrin31266.ecommercemultivendor.presentation.nav


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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
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
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.HomeScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.LoginScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.OrdersScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.ProductDetailsScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.ProductsScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.SearchScreen
import com.nrin31266.ecommercemultivendor.presentation.customer.screen.SignupScreen

@Composable
fun App(
//    authViewModel: AuthViewModel = hiltViewModel(),
////    sellerViewModel: SellerViewModel = hiltViewModel(),
////    userViewModel: UserViewModel = hiltViewModel(),

) {
    val context = LocalContext.current
    val navController = rememberNavController()

    // Create AuthPreferences manually if not using Hilt inject
    val authPreferences = remember { AuthPreferences(context) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isShowBottomBar = remember { mutableStateOf(false) }


    LaunchedEffect(currentRoute) {
        isShowBottomBar.value = when (currentRoute) {
            CustomerRoutes.CustomerHomeScreen.route, CustomerRoutes.CustomerAccountScreen.route
                , CustomerRoutes.CustomerOrdersScreen.route-> true
            else -> false
        }
    }

    val jwtState by authPreferences.jwtFlow.collectAsState(initial = null)



    var startScreen by remember { mutableStateOf(SubNavigation.MainCustomerScreen.route) }

    LaunchedEffect(jwtState) {
        if (jwtState != null) {

            Log.d("App", "Logged in with JWT: $jwtState");

        }else{
            Log.d("App", "Not logged in");
        }
    }
    val customerBottomItems = BottomNavItemsProvider.getCustomerBottomItems();







    Scaffold(
        bottomBar = {
            if(isShowBottomBar.value){
                CustomAnimatedBottomBar(navController, currentRoute, customerBottomItems)
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

                //  User NavGraph
                navigation(
                    route = SubNavigation.MainCustomerScreen.route,
                    startDestination = CustomerRoutes.CustomerHomeScreen.route
                ) {

                    composable(CustomerRoutes.CustomerHomeScreen.route) {
                        HomeScreen(navController)
                    }
                    composable(CustomerRoutes.CustomerAccountScreen.route) {
                        AccountScreen(navController)
                    }
                    composable(CustomerRoutes.CustomerOrdersScreen.route) {
                        OrdersScreen(navController)
                    }
                    composable(CustomerRoutes.SearchScreen.route){
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
                        ProductDetailsScreen(productId = productId!!, navController = navController)
                    }



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
                        redirect = redirect
                    )
                }


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
                            popUpTo(0) { inclusive = false }
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

    fun getCustomerBottomItems() = listOf(
        BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home, CustomerRoutes.CustomerHomeScreen.route),
        BottomNavItem("Orders", Icons.Filled.List, Icons.Outlined.List, CustomerRoutes.CustomerOrdersScreen.route),
        BottomNavItem("Account", Icons.Filled.Person, Icons.Outlined.Person, CustomerRoutes.CustomerAccountScreen.route)

    )


}
