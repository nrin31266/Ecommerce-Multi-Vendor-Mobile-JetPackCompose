package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.presentation.components.purchased.EmptySection
import com.nrin31266.ecommercemultivendor.presentation.components.rating.RatingCardItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.ReviewViewModel
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType

@Composable
fun ReviewScreen(
    navController: NavController,
    reviewViewModel: ReviewViewModel = hiltViewModel(),
    productId: Long
) {
    LaunchedEffect(Unit) {
        reviewViewModel.getRatingsOfProduct(productId)
    }
    val state = reviewViewModel.state.collectAsStateWithLifecycle()



    Scaffold (
        topBar = {
            CustomTopBar(
                title = "Rating",
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when{
                state.value.loading ->{
                    FullScreenLoading()
                }
                state.value.error.isNotEmpty() -> {
                    CustomMessageBox(type = MessageType.ERROR, message = state.value.error)
                }
                state.value.data.isEmpty() ->{
                    EmptySection("Review is empty")
                }
                else ->{
                    LazyColumn {
                        items(state.value.data.size) { index ->
                            val review = state.value.data[index]
                            RatingCardItem(review = review)
                        }
                    }
                }
            }
        }
    }
}