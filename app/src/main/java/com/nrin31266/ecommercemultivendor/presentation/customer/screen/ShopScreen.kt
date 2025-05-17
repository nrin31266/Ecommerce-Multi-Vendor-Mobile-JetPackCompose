package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto
import com.nrin31266.ecommercemultivendor.presentation.components.ProductItem
import com.nrin31266.ecommercemultivendor.presentation.components.purchased.EmptySection
import com.nrin31266.ecommercemultivendor.presentation.components.rating.RatingCardItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.ShopViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.ButtonSize
import com.nrin31266.ecommercemultivendor.presentation.utils.ButtonType
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomButton
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType

@Composable
fun ShopScreen(
    navController: NavController,
    shopViewModel: ShopViewModel = hiltViewModel(),
    shopId: Long
) {
    LaunchedEffect(Unit) {
        shopViewModel.getShopInfo(shopId)
        shopViewModel.getShopProducts(shopId)
    }
    val state = shopViewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                state.value.shopInfoLoading -> {
                    FullScreenLoading()
                }

                state.value.shopInfoError.isNotEmpty() -> {
                    CustomMessageBox(type = MessageType.ERROR, message = state.value.shopInfoError)
                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AsyncImage(
                                    model = state.value.shopInfo?.businessDetails?.banner
                                        ?: R.drawable.shop_banner_deffault,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp),
                                    contentScale = ContentScale.FillWidth
                                )
                                // Logo nằm nửa trong nửa ngoài
                                AsyncImage(
                                    model = state.value.shopInfo?.businessDetails?.logo
                                        ?: R.drawable.avatar,
                                    contentDescription = "Shop Logo",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .align(Alignment.BottomStart) // nằm bên trái banner
                                        .offset(y = (90).dp, x = 12.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color.White, CircleShape)

                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart) // nằm bên trái banner
                                        .offset(y = (90).dp, x = 136.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 4.dp)
                                            .align(Alignment.BottomStart)
                                    ) {
                                        Text(
                                            text = state.value.shopInfo?.businessDetails?.businessName
                                                ?: "", fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = state.value.shopInfo?.businessDetails?.businessAddress
                                                ?: "", color = Color.Gray
                                        )
                                        CustomButton(
                                            size = ButtonSize.SMALL,
                                            type = ButtonType.FILLED,
                                            onClick = { /*TODO*/ },
                                            text = "Follow",
                                        )
                                    }

                                }
                            }
                        }

                        item {
                            Spacer(Modifier.size(100.dp))
                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                when {
                                    state.value.shopProductsLoading -> {
                                        FullScreenLoading()
                                    }

                                    state.value.shopProductsError.isNotEmpty() -> {
                                        CustomMessageBox(
                                            type = MessageType.ERROR,
                                            message = state.value.shopProductsError
                                        )
                                    }

                                    state.value.shopProducts.isEmpty() -> {
                                        EmptySection("Shop is empty")
                                    }

                                    else -> {
                                        val products = state.value.shopProducts
                                        val chunkedList = products.chunked(2) // Chia thành nhóm 2 phần tử

                                        Column (
                                            modifier = Modifier.fillMaxSize()
                                                .padding(12.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ){
                                            chunkedList.forEach {rowItems->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                                ) {
                                                    // Hiển thị từng item trong hàng (tối đa 2)
                                                    rowItems.forEach { product ->
                                                        ProductItem(
                                                            modifier = Modifier.weight(1f),
                                                            onClick = {
                                                                navController.navigate(
                                                                    CustomerRoutes.ProductDetailScreen.withPath(product.id)
                                                                )
                                                            },
                                                            item = product,
                                                            isShopItem = true
                                                        )

                                                    }
                                                    if (rowItems.size < 2) {
                                                        Spacer(modifier = Modifier.weight(1f))
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
            }
            // TopBar đè lên nội dung
            CustomTopBar(
                onBackClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(Color.Transparent),

                actionIcon = Icons.Default.MoreHoriz,
                onActionClick = {}
            )
        }


    }

}

