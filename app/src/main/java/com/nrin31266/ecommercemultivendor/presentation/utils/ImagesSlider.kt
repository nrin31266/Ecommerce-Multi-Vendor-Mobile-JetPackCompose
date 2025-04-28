package com.nrin31266.ecommercemultivendor.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.colorResource
import com.nrin31266.ecommercemultivendor.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImagesSlider(
    images: List<String>,
    initCurrentImage: Int = 0,
    thumbnailSize: Dp = 40.dp // cho phép chỉnh size thumbnail nếu muốn
) {
    val pagerState = rememberPagerState(
        initialPage = initCurrentImage.coerceIn(0, images.size - 1)
    )
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            HorizontalPager(
                count = images.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )
            }

            // Text hiển thị số lượng ảnh, nằm góc dưới bên phải
            Text(
                text = "${pagerState.currentPage + 1}/${images.size}",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .background(Color.White.copy(alpha = 0.4f))
                    .padding(horizontal = 8.dp, vertical = 4.dp).border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(start = 8.dp, end = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // LazyRow chứa ảnh nhỏ
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            itemsIndexed(images) { index, imageUrl ->
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(thumbnailSize)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = if (pagerState.currentPage == index) 2.dp else 0.dp,
                            color = if (pagerState.currentPage == index) colorResource(R.color.warning_orange) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
