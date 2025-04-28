package com.nrin31266.ecommercemultivendor.presentation.customer.screen


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
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

    )
    var openBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Điều khiển hiển thị Bottom Sheet
    fun showBottomSheet() {
        openBottomSheet = true
    }

    // State
    var currentSubProduct by remember { mutableStateOf<SubProductDto?>(null) }
    val selectedOptions = remember { mutableStateMapOf<String, String>() }
    val mapOptions = remember { mutableStateMapOf<String, List<String>>() }
    val mapKeySubProductImages = remember { mutableStateMapOf<String, String>() }
    val mapSubProducts = remember { mutableStateMapOf<Map<String, String>, SubProductDto>() }

    // Tìm subProduct đúng với selectedOptions
    fun findMatchingSubProduct(selectedOptions: Map<String, String>): SubProductDto? {
        return mapSubProducts.entries.firstOrNull { (optionMap, _) ->
            optionMap.all { (key, value) ->
                selectedOptions[key] == value
            }
        }?.value
    }

// Build data khi có product
    LaunchedEffect(state.value.currentProduct) {
        val product = state.value.currentProduct
        if (product != null && !product.isSubProduct && product.subProducts != null) {
            // Tạm thời map để gom các options
            val tempMap = mutableMapOf<String, MutableSet<String>>()

            // Duyệt qua từng subProduct
            product.subProducts!!.forEach { subProduct ->
                val optionMap = mutableMapOf<String, String>()

                subProduct.options?.forEach { option ->
                    val type = option.optionType?.value ?: return@forEach
                    val value = option.optionValue ?: return@forEach

                    tempMap.getOrPut(type) { mutableSetOf() }.add(value)
                    optionMap[type] = value

                    // Nếu có optionKey (ví dụ như Color) thì lưu image cho từng value
                    if (product.optionKey != null && mapKeySubProductImages[value] == null) {
                        mapKeySubProductImages[value] = subProduct.images?.firstOrNull() ?: ""
                    }
                }

                if (optionMap.isNotEmpty()) {
                    mapSubProducts[optionMap] = subProduct
                }
            }

            // Cập nhật mapOptions và reset selectedOptions
            mapOptions.clear()
            tempMap.forEach { (type, values) ->
                mapOptions[type] = values.toList()
                selectedOptions[type] = "" // reset ban đầu là rỗng
            }
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
    if (openBottomSheet && state.value.currentProduct != null) ProductBottomSheet(
        sheetState,
        onDismiss = {
            openBottomSheet = false
        },
        state.value.currentProduct!!,
        coroutineScope,
        mapOptions,
        selectedOptions,
        { type, value ->
            selectedOptions[type] = value
            currentSubProduct = findMatchingSubProduct(
                selectedOptions
            )
        },
        currentSubProduct = currentSubProduct,
        mapKeySubProductImages
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    product: ProductDto,
    scope: CoroutineScope,
    mapOptions: Map<String, List<String>>,
    selectedOptions: Map<String, String>,
    onSelectedOptionChange: (String, String) -> Unit,
    currentSubProduct: SubProductDto?,
    mapKeySubProductImages: Map<String, String>
) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val maxSheetHeight = screenHeight * 0.5f
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),

        modifier = Modifier,
        content = {

            Scaffold(
                topBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                                .height(110.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),


                            ) {
                            Column(
                                modifier = Modifier,

                                ) {
                                AsyncImage(
                                    model = currentSubProduct?.images?.firstOrNull()
                                        ?: product.images.firstOrNull(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(100.dp)
                                        .width(100.dp)
                                        .border(
                                            1.dp,
                                            Color.LightGray,
                                            RoundedCornerShape(8.dp)
                                        ),
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
                                        maxLines = 1,
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
                                    Text("Storage: ${if (currentSubProduct == null) product.subProducts?.sumOf { it.quantity ?: 0 } else currentSubProduct.quantity}")
                                }
                            }
                            Column(
                                modifier = Modifier,
                            ) {
                                IconButton({
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            onDismiss()
                                        }
                                    }
                                }) {
                                    Icon(imageVector = Icons.Default.Close, "")
                                }
                            }
                        }
                        androidx.compose.material.Divider()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                },
                bottomBar = {

                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material.Divider()
                        Button(
                            {},
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                colorResource(R.color.elegant_gold)

                            ),
                            enabled = currentSubProduct != null
                        ) {
                            Text("Add to cart")
                        }
                    }
                },
                modifier = Modifier.heightIn(max = maxSheetHeight) // <<=== QUAN TRỌNG
            ) { innerPadding ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)

                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        mapOptions.forEach { (type, values) ->
                            item {
                                Text(
                                    text = type,
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                                )
                                com.google.accompanist.flowlayout.FlowRow(
                                    mainAxisSpacing = 8.dp,
                                    crossAxisSpacing = 8.dp,
                                    modifier = Modifier
                                        .padding(8.dp)
                                ) {
                                    values.forEach { value ->
                                        Box(
                                            modifier = Modifier
                                                .padding(
                                                    start = 8.dp,
                                                    bottom = 8.dp,
                                                    end = 8.dp
                                                )
                                                .border(
                                                    1.dp,
                                                    if (selectedOptions[type] == value) colorResource(
                                                        R.color.teal_700
                                                    )
                                                    else Color.Transparent,
                                                    RoundedCornerShape(2.dp)
                                                )
                                                .background(
                                                    Color.LightGray.copy(alpha = 0.3f),
                                                    RoundedCornerShape(2.dp)
                                                )
                                                .clickable {
                                                    onSelectedOptionChange(
                                                        type,
                                                        if (selectedOptions[type] == value) "" else value
                                                    )
                                                }
                                                .clip(RoundedCornerShape(2.dp))
//                                                .widthIn(min = 80.dp, max = 200.dp),


                                        ) {

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                if (type == product.optionKey) {
                                                    AsyncImage(
                                                        model = mapKeySubProductImages[value],
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .height(40.dp)
                                                            .width(40.dp)
                                                            .padding(1.dp)
                                                    )
                                                }
                                                Text(
                                                    value,
                                                    modifier = Modifier.padding(8.dp),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = if (selectedOptions[type] == value) colorResource(
                                                        R.color.teal_700
                                                    )
                                                    else Color.Black,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }





                                androidx.compose.material.Divider(
                                    modifier = Modifier.padding(
                                        vertical = 8.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }


        },

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