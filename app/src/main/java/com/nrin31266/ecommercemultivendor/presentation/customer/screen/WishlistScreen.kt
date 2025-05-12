package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.components.ProductItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.AuthViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.WishlistViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType

@Composable
fun WishlistScreen(
    navController: NavController,
    wishlistViewModel: WishlistViewModel = hiltViewModel(),
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.userAuthState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        if (authState.value.isLogin) {
            wishlistViewModel.getUserWishlist()
        }
    }
    val state = wishlistViewModel.wishlistState.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding).fillMaxSize()
        ) {
            when {
                !authState.value.isLogin->{
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text("Not login", fontSize = 16.sp)
                    }
                }

                state.value.isLoading -> {
                    FullScreenLoading()
                }

                state.value.errorMessage?.isNotBlank() == true -> {
                    CustomMessageBox(message = state.value.errorMessage!!, type = MessageType.ERROR)
                }

                state.value.data.isEmpty() ->{
                    Box(modifier = Modifier.align(Alignment.Center)){
                        Text("Wishlist is empty")
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        val wishlist = state.value.data
                        itemsIndexed(wishlist){ index, item ->
                            ProductItem({
                                navController.navigate(
                                    CustomerRoutes.ProductDetailScreen.withPath(item.product.id)
                                )
                            }, item.product)
                        }
                    }
                }
            }
        }
    }
}