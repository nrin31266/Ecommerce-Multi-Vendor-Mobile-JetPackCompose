package com.nrin31266.ecommercemultivendor.presentation.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

import com.nrin31266.ecommercemultivendor.domain.dto.HomeCategoryDto
@Composable
fun HomeCategoryItem(
    homeCategoryDto: HomeCategoryDto,
    onClick: () -> Unit
) {
    Column (modifier = Modifier.width(80.dp).clickable { onClick() }){
        AsyncImage(
            model = homeCategoryDto.image,
            contentDescription = homeCategoryDto.name,
            modifier = Modifier.width(80.dp).height(80.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = homeCategoryDto.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}