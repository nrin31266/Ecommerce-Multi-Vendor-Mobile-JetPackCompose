package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.presentation.components.ProductItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.ProductsViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType
import com.nrin31266.ecommercemultivendor.presentation.utils.SearchBar
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.log

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
        viewModel.getProduct(search, category, sort)
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
                    SearchBar ({
                        if (search != null) {
                            navController.popBackStack()
                        }else{
                            navController.navigate(CustomerRoutes.SearchScreen.route)
                        }
                    }, hint = search?:"Search now...")
                },
                onActionClick = {

                },
                actionIcon = Icons.Default.FilterAlt

            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Box (
            modifier = Modifier
                .padding(innerPadding)
        ){
            when{
                state.value.isLoading-> {
                    FullScreenLoading()
                }
                state.value.errorMessage?.isNotBlank() == true -> {
                    CustomMessageBox(message = state.value.errorMessage!!, type = MessageType.ERROR)
                }
                else -> {
                    LazyVerticalGrid (
                        columns = GridCells.Fixed(2),
                        state = gridState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        itemsIndexed(state.value.products) { index, product ->
                            ProductItem(
                                item = product,
                                onClick = {
                                    if(product.id!= null){
                                        navController.navigate(CustomerRoutes.ProductDetailScreen.withPath(product.id!!))
                                    }
                                }
                            )
                        }

                        item (
                            span = { GridItemSpan(2) }
                        ){
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                                    .padding(8.dp)
                            ){
                                // Hiển thị loading more ở cuối nếu đang load thêm
                                if(state.value.isLoadMoreLoading == true){
                                    Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
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
                                }else if(state.value.lastPage){
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
}