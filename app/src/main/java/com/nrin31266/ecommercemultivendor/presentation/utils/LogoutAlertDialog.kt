package com.nrin31266.ecommercemultivendor.presentation.utils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nrin31266.ecommercemultivendor.R


@Composable
@Preview(showSystemUi = true)
fun LogoutAlertDialog(
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
){
    Dialog (onDismissRequest = { onDismiss() }){
        Card (
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ){
            Column(modifier = Modifier.padding(16.dp)
                .fillMaxWidth()
                , horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.Default.Person, ""
                    , modifier = Modifier.size(80.dp).clip(CircleShape))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Are you sure you want to logout?", fontSize = 16.sp,fontWeight = FontWeight.Bold, color = colorResource(id = R.color.elegant_gold))
                Spacer(modifier = Modifier.height(16.dp))
                Row (
                    modifier = Modifier.fillMaxWidth()
                    , horizontalArrangement = Arrangement.SpaceBetween
                ){
                    OutlinedButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(8.dp)

                    ) {
                        Text(text = "Cancel", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.weight(0.1f))
                    Button(
                        onClick = { onConfirm() },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(8.dp)
                        , colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.elegant_gold)
                        )

                    ) {
                        Text("Ok", fontSize = 20.sp)
                    }
                }

            }
        }
    }

}