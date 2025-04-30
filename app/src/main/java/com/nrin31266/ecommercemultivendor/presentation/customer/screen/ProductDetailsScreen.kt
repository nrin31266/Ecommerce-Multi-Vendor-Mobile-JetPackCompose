package com.nrin31266.ecommercemultivendor.presentation.customer.screen


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.domain.dto.SubProductDto
import com.nrin31266.ecommercemultivendor.presentation.components.product.ProductBottomSheet
import com.nrin31266.ecommercemultivendor.presentation.components.product.ProductDetailsBottomBar
import com.nrin31266.ecommercemultivendor.presentation.components.product.ProductDetailsContent
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.ProductDetailsViewModel
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.ImagesSlider
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    viewModel: ProductDetailsViewModel = hiltViewModel(),
    navController: NavController,
    productId: String
) {
    LaunchedEffect(Unit) {
        Log.d("ProductDetailsScreen", "Product ID: $productId")
        viewModel.getProductDetails(productId.toLong())
    }
    val state = viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,        // <<— nếu bạn muốn bỏ qua trạng thái nửa màn hình,
//        confirmValueChange = { it != SheetValue.Hidden }
    )

    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (state.value.currentProduct != null) ProductDetailsBottomBar(
                state.value.currentProduct!!,
                onAddToCartClick = { viewModel.changeIsSheetBottom() })
        }
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
                    LazyColumn {
                        item {
                            ImagesSlider(product.images)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            ProductDetailsContent(product, state.value.favCurrentProduct)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        item {
                            androidx.compose.material.Divider()
                            Spacer(modifier = Modifier.height(8.dp))
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
                    .background(Color.Transparent)
            )
        }
    }
    val mapOptions = viewModel.mapOptions
    val selectedOptions = viewModel.selectedOptions
    val mapSubProducts = viewModel.mapSubProducts
    val mapKeySubProductImages = viewModel.mapKeySubProductImages
    val mapKeyToOptionMap = viewModel.mapKeyToOptionMap
    val currentSubProductState = viewModel.currentSubProduct.collectAsStateWithLifecycle()

    if(state.value.isOpenSheetBottom && state.value.currentProduct != null) {
        ProductBottomSheet(
            sheetState,
            onDismiss = {
                viewModel.changeIsSheetBottom()
            },
            state.value.currentProduct!!,
            coroutineScope,
            mapOptions,
            selectedOptions,
            { type, value ->
                viewModel.updateSelectedOption(type, value)
            },
            currentSubProduct = currentSubProductState.value,
            mapKeySubProductImages,
            mapSubProducts,
            mapKeyToOptionMap,
            state.value.quantity,
            {
                viewModel.updateQuantity(it)
            }
        )
    }
}





