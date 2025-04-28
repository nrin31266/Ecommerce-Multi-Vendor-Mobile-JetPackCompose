package com.nrin31266.ecommercemultivendor.presentation.customer.screen


import android.util.Log
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.fununtils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.SubProductDto
import com.nrin31266.ecommercemultivendor.presentation.components.DiscountLabel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.ProductDetailsViewModel
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.ProductsViewModel
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.ImagesSlider
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
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
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    // Điều khiển hiển thị Bottom Sheet
    fun showBottomSheet() {
        scope.launch {
            bottomSheetState.show()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (state.value.currentProduct != null) ProductDetailsBottomBar(
                state.value.currentProduct!!,
                onAddToCartClick = { showBottomSheet() })
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
    if (state.value.currentProduct != null) ModalBottomSheet(
        bottomSheetState,
        state.value.currentProduct!!,
        onClose = {
            scope.launch {
                bottomSheetState.hide()
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheet(
    bottomSheetState: ModalBottomSheetState,
    product: ProductDto,
    onClose: () -> Unit = {}
) {
    val currentSubProduct: SubProductDto? = null;

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            Scaffold(
                topBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .height(110.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Column(
                            modifier = Modifier,

                            ) {
                            AsyncImage(
                                model = product.images[0],
                                contentDescription = null,
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(100.dp)
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Fit,

                                )

                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Bottom,


                            ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)

                            ) {
                                Text("${
                                    if (currentSubProduct == null) CurrencyConverter.toVND(
                                        product.minSellingPrice
                                    ) + " - "
                                            + CurrencyConverter.toVND(product.maxMrpPrice) else currentSubProduct.sellingPrice?.let {
                                        CurrencyConverter.toVND(
                                            it
                                        )
                                    }
                                }",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(R.color.elegant_gold))
                                if (currentSubProduct?.mrpPrice != null) {
                                    Text(
                                        text = CurrencyConverter.toVND(currentSubProduct.mrpPrice!!),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray,
                                        textDecoration = TextDecoration.LineThrough,
                                    )
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                    Text("Storage: ${if (currentSubProduct == null) product.subProducts?.sumOf { it.quantity?:0 } else currentSubProduct.quantity}")
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Bottom,
                        ) {
                            IconButton({
                                onClose()
                            }) {
                                Icon(imageVector = Icons.Default.Close, "")
                            }
                        }
                    }
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    item {
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")
                        Text("e")

                    }
                }
            }
        },
        content = {

        }
    )
}

@Composable
fun ProductDetailsBottomBar(product: ProductDto, onAddToCartClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .padding(
                bottom = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding()
            )
            .background(Color.LightGray)
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .background(colorResource(R.color.teal_700))
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Default.Chat, "", tint = Color.White)
            Text("Chat now", color = Color.White, style = MaterialTheme.typography.bodySmall)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .background(colorResource(R.color.elegant_gold))
                .fillMaxSize()
                .clickable { onAddToCartClick() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(imageVector = Icons.Default.ShoppingCart, "", tint = Color.White)
            Text("Add to cart", color = Color.White, style = MaterialTheme.typography.bodySmall)

        }
    }
}

@Composable
fun ProductDetailsContent(
    product: ProductDto, isFavorite: Boolean = false,
) {
    val subProducts = product.subProducts
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                "${if (!subProducts.isNullOrEmpty()) subProducts.size else 1} variations available",
                color = Color.Gray, style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
        Box(
            modifier = Modifier.padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        CurrencyConverter.toVND(product.minSellingPrice),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(R.color.elegant_gold),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        CurrencyConverter.toVND(product.maxMrpPrice),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                    DiscountLabel(product.discountPercentage)
                }
            }

            Column(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Sold: ${product.totalSold}", style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (isFavorite) Icon(
                        imageVector = Icons.Default.Favorite, "", modifier = Modifier.size(20.dp),
                        tint = colorResource(R.color.error_red)
                    ) else
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            "",
                            modifier = Modifier.size(20.dp)
                        )
                }

            }
        }

        Row(
            modifier = Modifier.padding(horizontal = 8.dp)

        ) {
            Text(
                product.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }

}