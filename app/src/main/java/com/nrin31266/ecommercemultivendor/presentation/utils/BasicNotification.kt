package com.nrin31266.ecommercemultivendor.presentation.utils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nrin31266.ecommercemultivendor.R


@Composable
@Preview(showSystemUi = true)
fun BasicNotification(
    onConfirm: () -> Unit = {},
    text: String = "Basic Notification"
){
    Dialog (onDismissRequest = {}){
        Card (
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(4.dp),
        ){
            Column(modifier = Modifier.padding(8.dp).padding(top = 8.dp)
                .fillMaxWidth()
                , horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = text, fontSize = 14.sp,fontWeight = FontWeight.SemiBold, color = Color.Gray)

            }
            Divider(modifier = Modifier.padding(top = 8.dp))
            TextButton({onConfirm()}, modifier = Modifier.fillMaxWidth().height(44.dp)) {
                Text(text = "Ok", color = colorResource(R.color.elegant_gold))
            }

        }
    }

}