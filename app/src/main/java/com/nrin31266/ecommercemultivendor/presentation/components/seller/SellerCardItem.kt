package com.nrin31266.ecommercemultivendor.presentation.components.seller

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto
@Composable
fun SellerCardItem(navigation: NavController, seller: SellerDto) {

    LaunchedEffect (Unit){
        Log.d("SellerCardItem", "Seller: $seller")
    }

    Row(
        modifier = Modifier.background(Color.White).padding(8.dp).fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Row(
           modifier = Modifier.weight(1f),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ,horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
       ) {
           AsyncImage(
               model = seller.businessDetails?.logo ?: R.drawable.avatar,
               contentDescription = null,
               modifier = Modifier.size(60.dp)
                   .border(1.dp, Color.LightGray, CircleShape)
                   .clip(CircleShape)
                   .background(Color.Transparent, CircleShape).padding(1.dp)
           )
           Column {
               Text(seller.businessDetails?.businessName?:"",
                   fontWeight = FontWeight.Bold, fontSize = 16.sp)
               Text(seller.pickupAddress?.province?:"Việt Nam", fontSize = 14.sp, color = Color.Gray)
           }

       }
        Column {
            OutlinedButton({},
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
            ) {
                Text("See All", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
            }
        }
    }
}