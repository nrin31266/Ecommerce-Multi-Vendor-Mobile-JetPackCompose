package com.nrin31266.ecommercemultivendor.presentation.components.address

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nrin31266.ecommercemultivendor.domain.dto.AddressDto

@Composable
fun AddressCardItem(
    address: AddressDto,
    onEditClick: () -> Unit = {},
    onClick:()->Unit= {},
    isSelected: Boolean = false,
    modifier: Modifier= Modifier,
    notLast: Boolean = false,
    isOnlyView: Boolean = false
) {

    Box(){
        Row(
            modifier = modifier
        ) {
            if(!isOnlyView){
                Column {
                    RadioButton(
                        selected = isSelected,
                        onClick = onClick
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f).
                drawBehind {
                    if(notLast){
                        val strokeWidth = 1.dp.toPx()
                        val y = size.height - strokeWidth / 2

                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
                }
            ) {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(address.name?:"",
                        fontWeight = FontWeight.Bold)
                    Box(modifier= Modifier.width(1.dp).height(18.dp).background(Color.LightGray))
                    Text(address.phoneNumber?:"")
                }
                Row {
                    Text(
                        address.street + ", " + address.ward + ", "
                                + address.district + ", " + address.province,
                        color = Color.Gray, maxLines = 2, overflow = TextOverflow.Ellipsis
                    )
                }
                if(address.isDefault!= null && address.isDefault== true){
                    Box(
                        modifier= Modifier.padding(bottom = 8.dp).border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)).padding(4.dp)
                    ){
                        Text("Default", color = MaterialTheme.colorScheme.primary)
                    }
                }

            }
        }
       if(!isOnlyView){
           Row(
               modifier = Modifier.align(Alignment.TopEnd)
           ) {
               Text("Edit", modifier = Modifier.clickable {  }.padding(end = 4.dp), color = MaterialTheme.colorScheme.primary)
           }
       }
    }

}