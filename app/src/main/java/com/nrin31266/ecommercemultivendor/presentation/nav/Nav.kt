package com.nrin31266.ecommercemultivendor.presentation.nav

//sealed class SubNavigation(val route: String) {
//
//    data object MainHomeSellerScreen : SubNavigation("main_home_seller_screen")
//    data object MainHomeUserScreen : SubNavigation("main_home_user_screen")
//    data object MainHomeAdminScreen : SubNavigation("main_home_admin_screen")
//
//}
sealed class SellerRoutes(val route: String) {
    data object SellerLoginScreen : SellerRoutes("seller_login_screen")
    data object SellerSignupScreen : SellerRoutes("seller_signup_screen")
    data object SellerHomeScreen : SellerRoutes("seller_home_screen")
}
sealed class UserRoutes(val route: String){
    data object UserLoginScreen : UserRoutes("user_login_screen")
    data object UserSignupScreen : UserRoutes("user_signup_screen")
    data object UserHomeScreen : UserRoutes("user_home_screen")
    data object UserAccountScreen : UserRoutes("user_account_screen")
}

sealed class AdminRoutes(val route: String){
    data object AdminHomeScreen : AdminRoutes("admin_home_screen")
}

