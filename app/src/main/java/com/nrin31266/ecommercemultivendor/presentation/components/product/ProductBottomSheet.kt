package com.nrin31266.ecommercemultivendor.presentation.components.product

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
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.fununtils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.dto.SubProductDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    mapKeySubProductImages: Map<String, String>,
    mapSubProducts: Map<String, SubProductDto>,
    mapKeyToOptionMap: Map<String, Map<String, String>>,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {


    fun isOptionAvailable(type: String, value: String): Boolean {
        if (selectedOptions[type] == value) return true

        // Tạo bản sao tạm thời với lựa chọn được cập nhật
        val tempSelection = selectedOptions.toMutableMap().apply {
            this[type] = value
        }

        // Duyệt tất cả key trong mapSubProducts
        return mapSubProducts.entries.any { (keyString, subProduct) ->

            val optionMap = mapKeyToOptionMap[keyString] ?: return@any false

            // Kiểm tra xem optionMap có chứa hết tempSelection không
            optionMap.all { (k, v) ->
                tempSelection[k] == v || !tempSelection.containsKey(k)
            } && (subProduct.quantity ?: 0) > 0 && (subProduct.quantity ?: 0) >= quantity
        }
    }


    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val maxSheetHeight = screenHeight * 0.8f
    ModalBottomSheet(
        windowInsets = WindowInsets(0),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier,
        dragHandle = {},
        content = {

            Scaffold(
                modifier = Modifier.heightIn(max = maxSheetHeight), // <<=== QUAN TRỌNG
                topBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
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
                                    model = if (product.optionKey != null) {
                                        val selectedValue = selectedOptions[product.optionKey]
                                        val imageUrl = mapKeySubProductImages[selectedValue]
                                        imageUrl ?: product.images.firstOrNull()
                                    } else {
                                        product.images.firstOrNull()
                                    },


                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(100.dp)
                                        .width(100.dp)
                                        .border(
                                            1.dp,
                                            Color.LightGray,
                                            RoundedCornerShape(8.dp)
                                        ).clip(RoundedCornerShape(8.dp)) // Cắt ảnh bo góc
                                        .background(Color.Transparent, RoundedCornerShape(8.dp)),
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
                                        if (product.isSubProduct) {
                                            product.subProducts?.get(0)?.sellingPrice?.let {
                                                CurrencyConverter.toVND(
                                                    it
                                                )
                                            }
                                        } else
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

                    Column (
                        Modifier.padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                    ){
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material.Divider()
                        Button(
                            {},
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                colorResource(R.color.elegant_gold)

                            ),
                            enabled =
                            if (product.isSubProduct) {
                                quantity > 0 && quantity <= (product.subProducts?.get(0)?.quantity
                                    ?: 0)
                            } else {
                                currentSubProduct != null &&
                                        quantity > 0 &&
                                        quantity <= (currentSubProduct.quantity ?: 0)
                            }
                        ) {
                            Text(
                                "Add to cart",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                },

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
                                        val isAvailable = isOptionAvailable(type, value)
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
                                                    if (isAvailable) Color.LightGray.copy(alpha = 0.2f) else Color.LightGray,
                                                    RoundedCornerShape(2.dp)
                                                )
                                                .clickable(
                                                    enabled = isAvailable
                                                ) {
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
                                                            .padding(start = 4.dp)
                                                            .clip(
                                                                RoundedCornerShape(4.dp)
                                                            )
                                                            .background(
                                                                Color.Transparent
                                                            ),


                                                        )
                                                }
                                                Text(
                                                    value,
                                                    modifier = Modifier.padding(8.dp),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = if (!isAvailable) Color.Gray else if (selectedOptions[type] == value) colorResource(
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
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween // Quantity trái, icon phải
                            ) {
                                // Cột bên trái
                                Column {
                                    Text(
                                        "Quantity:",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Box {
                                    // Nội dung chính
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                    ) {
                                        IconButton(
                                            onClick = {
                                                if (quantity > 1) onQuantityChange(quantity - 1)
                                            },
                                            enabled = (currentSubProduct != null || product.isSubProduct) &&
                                                    quantity > 1
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Remove,
                                                contentDescription = null
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .background(Color.LightGray)
                                                .width(1.dp)
                                                .height(46.dp)
                                        )

                                        Text(
                                            quantity.toString(),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(horizontal = 24.dp),
                                            color = colorResource(R.color.elegant_gold)
                                        )

                                        Box(
                                            modifier = Modifier
                                                .background(Color.LightGray)
                                                .width(1.dp)
                                                .height(46.dp)
                                        )

                                        IconButton(
                                            onClick = {
                                                onQuantityChange(quantity + 1)
                                            }, enabled = if (product.isSubProduct) {

                                                quantity < (product.subProducts?.get(0)?.quantity
                                                    ?: 0)

                                            } else {
                                                quantity < (currentSubProduct?.quantity ?: 0)

                                            }

                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = null
                                            )
                                        }
                                    }

                                    // Lớp phủ + chặn tương tác
                                    if (currentSubProduct == null && !product.isSubProduct) {
                                        Box(
                                            modifier = Modifier
                                                .matchParentSize()
                                                .background(
                                                    Color.LightGray.copy(0.5f),
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .clip(RoundedCornerShape(8.dp)) // Màu xám mờ
                                                .pointerInput(Unit) {} // Ngăn người dùng tương tác
                                        )
                                    }
                                }
                            }


                        }
                    }
                }
            }


        },
//        dragHandle = {}
    )
}