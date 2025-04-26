package com.nrin31266.ecommercemultivendor.presentation.nav

sealed class SubNavigation(val route: String) {


    data object MainAuthScreen : SubNavigation("main_auth_screen")
    data object MainCustomerScreen : SubNavigation("main_customer_screen")

}

sealed class CustomerRoutes(val route: String){
    data object CustomerLoginScreen : CustomerRoutes("customer_login_screen")
    data object CustomerSignupScreen : CustomerRoutes("customer_signup_screen")
    data object CustomerHomeScreen : CustomerRoutes("customer_home_screen")
    data object CustomerAccountScreen : CustomerRoutes("customer_account_screen")
    data object CustomerOrdersScreen : CustomerRoutes("customer_orders_screen")
    data object SearchScreen : CustomerRoutes("search_screen")
}


