package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.constant.PRICE_FILTER
import com.nrin31266.ecommercemultivendor.common.constant.RATING_FILTER
import com.nrin31266.ecommercemultivendor.common.constant.SORT_PRODUCTS
import com.nrin31266.ecommercemultivendor.presentation.components.ProductItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.ProductsViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.ButtonType
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomButton
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType
import com.nrin31266.ecommercemultivendor.presentation.utils.SearchBar
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel = hiltViewModel(),
    navController: NavController,
    search: String? = null,
    category: String? = null,
    sort: String? = null
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        Log.d("ProductsScreen", "Query: $search, Category: $category, Sort: $sort")
//        viewModel.getProduct(search, category, sort)
    }

    // Theo dõi động vị trí cuộn, trigger loadMore mỗi khi cuối danh sách
    LaunchedEffect(gridState) {
        snapshotFlow {
            // Lấy index của item cuối cùng đang hiển thị
            gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                val totalItems = state.value.products.size
                // Nếu đã gần đến cuối, chưa ở lastPage và đang không loadMore
                if (lastVisibleIndex >= totalItems - 1
                    && !state.value.lastPage
                    && !state.value.isLoadMoreLoading
                ) {
                    viewModel.loadMoreProducts()
                }
            }
    }


    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
                content = {
                    SearchBar({
                        if (search != null) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(CustomerRoutes.SearchScreen.route)
                        }
                    }, hint = search ?: "Search now...")

                },
                onActionClick = {
                    viewModel.onToggleRightSheet()
                },
                actionIcon = Icons.Default.FilterAlt,
                extraContent = {
                    SortSection(viewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                },
                hasDivider = true

            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            when {
                state.value.isLoading -> {
                    FullScreenLoading()
                }

                state.value.errorMessage?.isNotBlank() == true -> {
                    CustomMessageBox(message = state.value.errorMessage!!, type = MessageType.ERROR)
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        itemsIndexed(state.value.products) { index, product ->
                            ProductItem(
                                item = product,
                                onClick = {
                                    if (product.id != null) {
                                        navController.navigate(
                                            CustomerRoutes.ProductDetailScreen.withPath(
                                                product.id!!
                                            )
                                        )
                                    }
                                }
                            )
                        }

                        item(
                            span = { GridItemSpan(2) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                                    .padding(8.dp)
                            ) {
                                // Hiển thị loading more ở cuối nếu đang load thêm
                                if (state.value.isLoadMoreLoading) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = colorResource(R.color.warning_orange),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.size(8.dp))
                                        Text(
                                            text = "Loading more...",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = colorResource(R.color.warning_orange)
                                        )
                                    }
                                } else if (state.value.lastPage) {
                                    Text(
                                        text = "No more products",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.align(Alignment.Center),
                                        color = colorResource(R.color.warning_orange)
                                    )
                                }

                            }
                        }

                    }

                }
            }
        }

    }
    ProductsRightSideSheet(viewModel)

}

data class PriceFilter(
    val label: String,
    val value: PRICE_FILTER
)

data class RatingFilter(
    val label: String,
    val value: RATING_FILTER
)

data class SortProduct(
    val label: String,
    val value: SORT_PRODUCTS
)

@Composable
fun SortSection( viewModel: ProductsViewModel){
    val state = viewModel.state.collectAsStateWithLifecycle()

    val sortProductList = listOf(
        SortProduct("Default", SORT_PRODUCTS.DEFAULT),
        SortProduct("New", SORT_PRODUCTS.NEW),
        SortProduct("Bestseller", SORT_PRODUCTS.BESTSELLER),
        SortProduct("Price ▲", SORT_PRODUCTS.PRICE_LOW),
        SortProduct("Price ▼", SORT_PRODUCTS.PRICE_HIGH),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        sortProductList.forEach {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.Center) // Để text căn giữa trong phần được chia
                    .clickable {
                        viewModel.onSort(it.value)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = it.label,
                    fontSize = 14.sp,
                    color = if (state.value.sort == it.value) MaterialTheme.colorScheme.primary else Color.Black
                )
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .height(3.dp)
                        .fillMaxWidth(0.8f) // Cho underline nhỏ gọn hơn text
                        .background(
                            if (state.value.sort == it.value) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }


}




@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductsRightSideSheet(
    viewModel: ProductsViewModel
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        // Nền mờ phía sau sheet
        if (state.value.isVisibleRightSheet) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(onClick = { viewModel.onToggleRightSheet() }) // Click ngoài sheet => đóng
            )
        }

        // Sheet trượt từ phải
        AnimatedVisibility(
            visible = state.value.isVisibleRightSheet,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.85f)
                .align(Alignment.CenterEnd) // nằm ở mép phải
        ) {
            Scaffold(
                topBar = {
                    CustomTopBar(
                        content = {
                            Text(
                                "Filter",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        },
                        hasDivider = true

                    )
                },
                bottomBar = {
                    Column(
                        modifier = Modifier.padding(
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding()
                        ), verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Divider()
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CustomButton(type = ButtonType.OUTLINED, text = "Reset", onClick = {
                                viewModel.onResetFilter()
                            }, modifier = Modifier.weight(1f))
                            CustomButton(text = "Apply", onClick = {
                                viewModel.onFilter()
                            }, modifier = Modifier.weight(1f))
                        }
                    }
                }
            ) { ip ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(ip),
                    verticalArrangement = Arrangement.Top
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            val priceFilters = listOf<PriceFilter>(
                                PriceFilter("Less than 50K", PRICE_FILTER.LESS_THAN_50K),
                                PriceFilter("50K to 100K", PRICE_FILTER.FROM_50K_TO_100K),
                                PriceFilter("100K to 200K", PRICE_FILTER.FROM_100K_TO_200K),
                                PriceFilter("200K to 500K", PRICE_FILTER.FROM_200K_TO_500K),
                                PriceFilter("500K to 1000K", PRICE_FILTER.FROM_500K_TO_1000K),
                                PriceFilter("Greater than 1000K", PRICE_FILTER.GREATER_THAN_1000K)
                            )

                            Row (
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                Text("Price")
                                Icon(Icons.Default.Money, contentDescription = null)
                            }
                            androidx.compose.foundation.layout.FlowRow(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                maxItemsInEachRow = 2,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                priceFilters.forEach { filter ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.48f) // gần 50% để mỗi hàng 2 item có spacing
                                            .border(
                                                1.dp,
                                                if (state.value.priceFilter == filter.value) MaterialTheme.colorScheme.primary
                                                else Color.Transparent,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .clickable {
                                                viewModel.onChangeFilter(
                                                    ProductsViewModel.ProductFilter.Price(filter.value)
                                                )
                                            }
                                            .background(Color.LightGray.copy(0.2f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)


                                    ) {
                                        Text(filter.label, fontSize = 12.sp)
                                    }
                                }
                            }

                        }
                        Divider()
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            val ratingFilterList = listOf(
                                RatingFilter("From 1 star", RATING_FILTER.FROM_ONE_STAR),
                                RatingFilter("From 2 star", RATING_FILTER.FROM_TWO_STARS),
                                RatingFilter("From 3 star", RATING_FILTER.FROM_THREE_STARS),
                                RatingFilter("From 4 star", RATING_FILTER.FROM_FOUR_STARS),
                                RatingFilter("5 star", RATING_FILTER.FIVE_STARS)
                            )

                            Row (
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                Text("Rating")
                                Icon(Icons.Default.Star, contentDescription = null, tint = colorResource(R.color.elegant_gold))
                            }
                            androidx.compose.foundation.layout.FlowRow(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                maxItemsInEachRow = 2,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ratingFilterList.forEach { filter ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.48f) // gần 50% để mỗi hàng 2 item có spacing
                                            .border(
                                                1.dp,
                                                if (state.value.ratingFilter == filter.value) MaterialTheme.colorScheme.primary
                                                else Color.Transparent,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .clickable {
                                                viewModel.onChangeFilter(
                                                    ProductsViewModel.ProductFilter.Rating(filter.value)
                                                )
                                            }
                                            .background(Color.LightGray.copy(0.2f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)


                                    ) {
                                        Text(filter.label, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
