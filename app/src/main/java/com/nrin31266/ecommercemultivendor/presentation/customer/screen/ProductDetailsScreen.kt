package com.nrin31266.ecommercemultivendor.presentation.customer.screen


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.domain.dto.request.AddUpdateCartItemRequest
import com.nrin31266.ecommercemultivendor.presentation.components.ProductItem
import com.nrin31266.ecommercemultivendor.presentation.components.product.ProductBottomSheet
import com.nrin31266.ecommercemultivendor.presentation.components.product.ProductDetailsBottomBar
import com.nrin31266.ecommercemultivendor.presentation.components.product.ProductDetailsContent
import com.nrin31266.ecommercemultivendor.presentation.components.seller.SellerCardItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.ProductDetailsViewModel
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.components.product.ImagesSlider
import com.nrin31266.ecommercemultivendor.presentation.components.rating.RatingCardItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.AuthViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.CartViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.BasicNotification
import com.nrin31266.ecommercemultivendor.presentation.utils.HeaderSection
import com.nrin31266.ecommercemultivendor.presentation.utils.IconButtonWithBadge
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel = hiltViewModel(),
    navController: NavController,
    productId: String,
    authViewModel: AuthViewModel,
    cartViewModel: CartViewModel
) {

    val showDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val authState = authViewModel.userAuthState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.destination?.route?.let {
            Log.d("NavController", "Current route: $it")
        }
        Log.d("ProductDetailsScreen", "Product ID: $productId")
        viewModel.getProductDetails(productId.toLong())
        viewModel.getFirstRating(productId.toLong())
        viewModel.getRelatedProducts(productId.toLong())
        if(authState.value.isLogin){
            viewModel.checkUserWishlist(productId.toLong())
        }



        cartViewModel.addProductToCartEventFlow.collectLatest {
            when(it){
                is ProductDetailsViewModel.ProductDetailsEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = it.message
                    )
                }
                is ProductDetailsViewModel.ProductDetailsEvent.ShowBasicDialog -> {
                    showDialog.value = true
                }
                null->{

                }
            }
        }
    }
    val state = viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,        // <<— nếu bạn muốn bỏ qua trạng thái nửa màn hình,
//        confirmValueChange = { it != SheetValue.Hidden }
    )

    val cartInfoState = cartViewModel.cartInfoState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (state.value.currentProduct != null) ProductDetailsBottomBar(
                state.value.currentProduct!!,
                onAddToCartClick = { viewModel.changeIsSheetBottom() })
        },

    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {

            when {
                state.value.isLoading -> {
                    FullScreenLoading()
                }

                state.value.errorMessage?.isNotBlank() == true -> {
                    CustomMessageBox(message = state.value.errorMessage!!, type = MessageType.ERROR)
                }

                state.value.currentProduct != null -> {
                    val product = state.value.currentProduct!!
                    LazyColumn (
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        item {
                            ImagesSlider(product.images)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            ProductDetailsContent(navController, authViewModel, viewModel)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        item {
                            Divider()
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        item {
                            ProductDetailsRating(
                                viewModel
                            )
                        }
                        if(product.seller!=null){
                            item {
                                SellerCardItem(navController, product.seller!!)
                            }
                        }
                        item{
                            ProductDescription(product.description)
                        }

                        item{
                            RelatedProductsSection(navController, viewModel)
                        }
                    }
                }


            }
            // TopBar đè lên nội dung
            CustomTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(Color.Transparent),
                customAction = {
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
                        Icon(Icons.Default.ShoppingCart, "Spc")
                    }
                }
            )
        }
    }


    val optionState = viewModel.productOptionState.collectAsStateWithLifecycle()
    val addProductToCartState = cartViewModel.addProductToCartState.collectAsStateWithLifecycle()

    if(state.value.isOpenSheetBottom && state.value.currentProduct != null) {
        ProductBottomSheet(
            sheetState,
            onDismiss = {
                viewModel.changeIsSheetBottom()
            },
            state.value.currentProduct!!,
            coroutineScope,
            optionState.value.mapOptions,
            optionState.value.selectedOptions,
            { type, value ->
                viewModel.updateSelectedOption(type, value)
            },
            currentSubProduct = state.value.currentSubProduct,
            optionState.value.mapKeySubProductImages,
            optionState.value.mapSubProducts,
            optionState.value.mapKeyToOptionMap,
            state.value.quantity,
            {
                viewModel.updateQuantity(it)
            },
            authState.value.isLogin,
            navController,
            addProductToCartState.value.isLoading,
            addProductToCartState.value.errorMessage,
            onAddToCartClick = {
                cartViewModel.addProductToCart(state.value.currentProduct?.id!!,
                    state.value.currentSubProduct?.id!!,
                    AddUpdateCartItemRequest(state.value.quantity))
            },
            snackbarHostState
        )
    }

    if(showDialog.value){
        BasicNotification({showDialog.value = false}, addProductToCartState.value.errorMessage?:"")
    }
}

@Composable
fun RelatedProductsSection(navController: NavController, viewModel: ProductDetailsViewModel) {
    val relatedProductsState = viewModel.relatedProductsState.collectAsStateWithLifecycle()

    Column (
        modifier = Modifier.background(Color.White).fillMaxWidth()
    ){
        Row (
            modifier = Modifier.padding(8.dp)
        ){
            Text("Related products", style = MaterialTheme.typography.titleMedium)
        }
        Divider()
        LazyRow (
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            if(relatedProductsState.value.data.isNotEmpty()){
                items(relatedProductsState.value.data){
                    ProductItem({
                        navController.navigate(CustomerRoutes.ProductDetailScreen.withPath(it.id))
                    }, it, modifier = Modifier.width(165.dp))
                }
            }else{
               item {
                   Text("Not related products yet")
               }
            }

        }
    }

}


@Composable
fun ProductDescription(description: String) {

    val showMore = rememberSaveable(description) { mutableStateOf(false) }


    Column (
        modifier = Modifier.background(Color.White).fillMaxWidth()
    ){
        Row (modifier = Modifier.padding(8.dp)){
            Text("Description", style = MaterialTheme.typography.titleMedium)
        }
        Divider()
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
           Text(if (description.length < 200) description else if (showMore.value) description else description.substring(0, 200) + "..."
           , fontSize = 15.sp)

        }
        Divider()
        if(description.length>=200){
            Row (modifier = Modifier.fillMaxWidth().padding(8.dp).clickable {
                showMore.value = !showMore.value
            },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                var text = ""
                var icon: ImageVector
                if(showMore.value){
                    text = "Show less"
                    icon = Icons.Default.KeyboardArrowUp
                }else{
                    text = "Show more"
                    icon = Icons.Default.KeyboardArrowDown
                }
                Text(text, textAlign = TextAlign.Center, color = Color.Gray)
                Icon(icon, "des",)
            }
        }
    }

}

@Composable
fun ProductDetailsRating(
    viewModel: ProductDetailsViewModel
){
    val ratingState = viewModel.productRatingState.collectAsStateWithLifecycle()
    val state = viewModel.state.collectAsStateWithLifecycle()

    val product = state.value.currentProduct
    Column (
        modifier = Modifier.background(Color.White)
    ){
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            HeaderSection(
                extraTitle = {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ){
                        Text("${product?.avgRating?:0}",
                            style = MaterialTheme.typography.titleMedium)
                        Icon(imageVector = Icons.Default.Star, "",
                            tint = colorResource(R.color.elegant_gold)
                        )
                        Box(modifier = Modifier
                            .width(2.dp)
                            .height(20.dp)
                            .background(Color.LightGray),)
                        Text("${product?.numberRating} rated",
                            style = MaterialTheme.typography.bodyMedium)
                    }
                },
                actionName = "All"
            )
        }
        Divider()
        Column (
            modifier = Modifier.padding(8.dp)
        ){
            val listRating = ratingState.value.data
            if(!listRating.isNullOrEmpty()){
                Text("New rating", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                listRating.forEach {
                    RatingCardItem(it) // không dùng key, không dùng map
                }

            }else if(listRating.isNullOrEmpty()  && !ratingState.value.isLoading){
                Text("No rating")
            }
        }
    }
}



