package com.nrin31266.ecommercemultivendor.presentation.components.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.R
import com.nrin31266.ecommercemultivendor.common.fununtils.CurrencyConverter
import com.nrin31266.ecommercemultivendor.domain.dto.response.ShopCartGroupResponse

@Composable
fun ShopCheckoutItem(
    navController: NavController,
    shopGroup: ShopCartGroupResponse
) {

    Card(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .shadow(
                1.dp,
                spotColor = Color.LightGray
            )
            .clip(RoundedCornerShape(4.dp)),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ), elevation = CardDefaults.cardElevation(12.dp))
    {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row {
                shopGroup.seller.businessDetails?.businessName?.let { Text(it) }
            }

        }
        Divider()
        Column {
            shopGroup.cartItems.forEach{
                CheckoutProductItem(
                    navController = navController,
                    cartItemDto = it
                )
            }
        }
        Divider()
        Column (
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            Row (modifier = Modifier.fillMaxWidth()){
                Text("Voucher by shop", modifier = Modifier.weight(1f))
                Row (modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically){
                    Text("Select or enter coupon",
                        color = Color.Gray)
                    Icon(imageVector = Icons.Default.ArrowForwardIos, "",
                        tint = Color.Gray, modifier = Modifier.size(12.dp))
                }
            }
            Row (modifier = Modifier.fillMaxWidth()){
                Text("Message to shop", modifier = Modifier.weight(1f))
                Row (modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically){
                    Text("Enter message",
                        color = Color.Gray)
                    Icon(imageVector = Icons.Default.ArrowForwardIos, "",
                        tint = Color.Gray, modifier = Modifier.size(12.dp))
                }
            }
        }
        Divider()
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text("Shipping method", modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Row (modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically){
                    Text("All",
                        color = Color.Gray)
                    Icon(imageVector = Icons.Default.ArrowForwardIos, "",
                        tint = Color.Gray, modifier = Modifier.size(12.dp))
                }
            }
            Box(
                modifier = Modifier.border(1.dp, colorResource(R.color.teal_700), RoundedCornerShape(4.dp))
                    .background(colorResource(R.color.success_green).copy(0.1f)).padding(8.dp)

            ){
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Text("Self-shipping sellers", modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text(CurrencyConverter.toVND(30000), fontSize = 13.sp)

                }
            }
        }
        Divider()
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Row (
                modifier = Modifier,
            ){
                Text("Total (" + shopGroup.cartItems.size + "item" + (if (shopGroup.cartItems.size > 1) "s" else "") + ")",
                    modifier = Modifier.weight(1f))
                val total = shopGroup.cartItems.sumOf { it.subProduct?.sellingPrice?.times(it.quantity!!) ?: 0 }
                Row {
                    Text(CurrencyConverter.toVND(total + 30000),
                        color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold
                    )
                }
            }

        }

    }
}