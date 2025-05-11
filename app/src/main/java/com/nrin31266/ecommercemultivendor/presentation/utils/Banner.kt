package com.nrin31266.ecommercemultivendor.presentation.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect as LaunchedEffect1
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.domain.dto.BannerDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banner(banners: List<BannerDto>) {
    if (banners.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { banners.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect1(Unit) {
        while (true) {
            delay(2000)
            val nextPage = (pagerState.currentPage + 1) % banners.size
            scope.launch {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier.wrapContentSize()) {
            BannerPager(banners = banners, pagerState = pagerState)

        }
        PageIndicator(
            pageCount = banners.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BannerPager(banners: List<BannerDto>, pagerState: PagerState) {
    HorizontalPager(
        state = pagerState, modifier = Modifier.wrapContentSize()
    ) { currentPage ->
        BannerItem(banner = banners[currentPage])
    }
}

@Composable
private fun BannerItem(banner: BannerDto) {
    Card(
        modifier = Modifier
            .height(230.dp)
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp).clickable {

            },
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        AsyncImage(
            model = banner.imageUrl,
            contentDescription = banner.title,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
    }
}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(top = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) {
            IndicatorDot(isSelected = it == currentPage, modifier)
        }
    }
}

@Composable
fun SelectedDot(modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(5.dp))
            .padding(2.dp)
            .height(10.dp)
            .width(28.dp)
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                shape = RoundedCornerShape(5.dp)
            )
    ) {

    }
}

@Composable
fun IndicatorDot(isSelected: Boolean, modifier: Modifier) {
    if (isSelected) {
        SelectedDot(modifier)
    } else {
        Box(
            modifier = modifier
                .padding(2.dp)
                .clip(shape = CircleShape)
                .size(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    shape = CircleShape
                )
        ) {

        }
    }
}

