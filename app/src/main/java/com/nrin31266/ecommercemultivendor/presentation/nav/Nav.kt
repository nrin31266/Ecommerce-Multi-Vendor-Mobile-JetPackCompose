package com.nrin31266.ecommercemultivendor.presentation.nav

sealed class SubNavigation(val route: String) {


    data object MainAuthScreen : SubNavigation("main_auth_screen")
    data object MainCustomerScreen : SubNavigation("main_customer_screen")

}

sealed class CustomerRoutes(val route: String){
    data object CustomerLoginScreen : CustomerRoutes("customer_login_screen?redirect={redirect}"){
        fun withRedirect(redirect: String): String {
            return "customer_login_screen?redirect=$redirect"
        }
    }
    data object CustomerSignupScreen : CustomerRoutes("customer_signup_screen"){
        fun withRedirect(redirect: String): String {
            return "customer_signup_screen?redirect=$redirect"
        }
    }
    data object CustomerHomeScreen : CustomerRoutes("customer_home_screen")
    data object CustomerAccountScreen : CustomerRoutes("customer_account_screen")
    data object CustomerOrdersScreen : CustomerRoutes("customer_orders_screen")
    data object SearchScreen : CustomerRoutes("search_screen")
    data object ProductsScreen : CustomerRoutes("products_screen") {
        fun withQuery(search: String?=null, category: String?=null, sort: String?=null): String {
            return "products_screen?search=${search.orEmpty()}&category=${category.orEmpty()}&sort=${sort.orEmpty()}"
        }
    }


    data object ProductDetailScreen : CustomerRoutes("product_detail_screen/{productId}") {
        fun withPath(productId: Long): String {
            return "product_detail_screen/$productId"
        }
    }

    data object CartScreen : CustomerRoutes("cart_screen")
    data object CheckoutScreen : CustomerRoutes("checkout_screen")
    data object AddressScreen : CustomerRoutes("address_screen")
    data object AddEditAddressScreen : CustomerRoutes("add_edit_address_screen/{addressId}"){
        fun withPath(addressId: Long? = -1): String {
            return "add_edit_address_screen/$addressId"
        }
    }
    data object PurchasedScreen : CustomerRoutes("purchased_screen/{tabIndex}"){
        fun withPath(tabIndex: Int): String {
            return "purchased_screen/$tabIndex"
        }
    }
    data object SellerOrderDetailsScreen : CustomerRoutes("seller_order_details_screen/{sellerOrderId}"){
        fun withPath(sellerOrderId: Long): String {
            return "seller_order_details_screen/$sellerOrderId"
        }
    }
    data object AddRatingScreen : CustomerRoutes("add_rating_screen/{orderId}"){
        fun withPath(orderId: Long): String {
            return "add_rating_screen/$orderId"
        }
    }

    data object WishlistScreen : CustomerRoutes("wishlist_screen")
    data object NotificationsScreen : CustomerRoutes("notifications_screen")

    data object PaymentWebViewScreen : CustomerRoutes("payment_webview_screen/{url}"){
        fun withLink(url: String): String {
            return "payment_webview_screen/$url"
        }
    }
    data object RatingScreen : CustomerRoutes("rating_screen/{productId}"){
        fun withPath(productId: Long): String {
            return "rating_screen/$productId"
        }
    }
    data object ShopScreen : CustomerRoutes("shop_screen/{shopId}"){
        fun withPath(shopId: Long): String {
            return "shop_screen/$shopId"
        }
    }


}


