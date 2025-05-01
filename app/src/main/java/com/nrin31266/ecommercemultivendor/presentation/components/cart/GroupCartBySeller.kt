package com.nrin31266.ecommercemultivendor.presentation.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.domain.dto.response.ShopCartGroupResponse

@Composable
fun GroupCartBySeller(
    navController: NavController,
    group: ShopCartGroupResponse
) {
    Card (
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
        , shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column(
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp),)  {
                Row (

                ){
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {  }.weight(1f)
                    ){
                        AsyncImage(
                            model = group.seller.businessDetails?.logo ?: R.drawable.avatar,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp).clip(CircleShape).border(1.dp, Color.LightGray, CircleShape),
                        )
                        Text(group.seller.businessDetails?.businessName?:"",
                            fontWeight = FontWeight.Bold
                        )
                        Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null,
                            Modifier.size(12.dp))
                    }
                    Text("${group.cartItems.size} item${if (group.cartItems.size > 1) "s" else ""}",
                        fontSize = 12.sp,)
                }
            }
            Divider(modifier = Modifier.padding(bottom = 8.dp))
            Column (
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                group.cartItems.map {
                    CartItem(navController, it)
                }
            }
        }

    }
}