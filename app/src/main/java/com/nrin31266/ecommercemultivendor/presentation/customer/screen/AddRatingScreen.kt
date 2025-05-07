package com.nrin31266.ecommercemultivendor.presentation.customer.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.nrin31266.ecommercemultivendor.domain.dto.request.CreateReviewRequest
import com.nrin31266.ecommercemultivendor.presentation.components.purchased.OrderItem
import com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel.AddRatingViewModel
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes
import com.nrin31266.ecommercemultivendor.presentation.utils.ButtonSize
import com.nrin31266.ecommercemultivendor.presentation.utils.ButtonType
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomButton
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomCard
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomMessageBox
import com.nrin31266.ecommercemultivendor.presentation.utils.CustomTopBar
import com.nrin31266.ecommercemultivendor.presentation.utils.FullScreenLoading
import com.nrin31266.ecommercemultivendor.presentation.utils.MessageType
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddRatingScreen(
    navController: NavController,
    orderId: Long,
    viewModel: AddRatingViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsStateWithLifecycle()
    val handleState = viewModel.handle.collectAsStateWithLifecycle()
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris -> imageUris = uris }
    )

    LaunchedEffect(Unit) {
        viewModel.getOrderItem(orderId)

        viewModel.event.collectLatest {
            when(it){
                is AddRatingViewModel.AddRatingEvent.Rated -> {
                    navController.navigate(CustomerRoutes.PurchasedScreen.withPath(4)){
                        popUpTo(CustomerRoutes.PurchasedScreen.route){
                            inclusive = true
                        }
                    }
                }
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
                    Text("Rating for product", style = MaterialTheme.typography.titleLarge)
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                state.value.isLoading -> {
                    FullScreenLoading()
                }

                state.value.errorMessage != null -> {
                    CustomMessageBox(state.value.errorMessage!!, type = MessageType.ERROR)
                }

                state.value.orderItem!=null -> {
                    val orderItem = state.value.orderItem
                    CustomCard {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            OrderItem(orderItem = orderItem!!)
                            Spacer(modifier = Modifier.height(16.dp))
                            Row {
                                (1..5).forEach { i ->
                                    Icon(
                                        imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clickable { rating = i },
                                        tint = Color(0xFFFFC107)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 📝 Comment
                            OutlinedTextField(
                                value = comment,
                                onValueChange = { comment = it },
                                label = { Text("Write review...") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // 📷 Chọn ảnh
                            CustomButton(
                                onClick = { launcher.launch("image/*") },
                                text = "Select images",
                                type = ButtonType.OUTLINED, size = ButtonSize.SMALL
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Hiển thị preview ảnh
                            LazyRow {
                                items(imageUris) { uri ->
                                    Image(
                                        painter = rememberAsyncImagePainter(uri),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .padding(4.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))


                            CustomButton(
                                onClick = {
                                    if(comment.trim().isEmpty()){
                                        return@CustomButton;
                                    }
                                    viewModel.addRating(
                                        orderItem?.id!!,
                                        CreateReviewRequest(
                                            reviewText = comment,
                                            reviewRating = rating,
                                        ),
                                         imageUris
                                    )
                                },
                                text = "Rate now",
                                modifier = Modifier.align(Alignment.End)
                                , loading = handleState.value.isLoading,
                                enabled = !handleState.value.isLoading
                            )
                        }
                    }
                }
            }
        }
    }


}
