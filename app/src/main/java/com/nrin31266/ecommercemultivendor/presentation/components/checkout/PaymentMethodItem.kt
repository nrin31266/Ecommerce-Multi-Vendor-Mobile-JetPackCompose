package com.nrin31266.ecommercemultivendor.presentation.components.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vanrin05.app.domain.PAYMENT_METHOD

@Composable
fun PaymentMethodItem(
    paymentMethod: PaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Row (
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
        ,verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
            ,verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = paymentMethod.icon), "",
                modifier = Modifier.size(24.dp)
            )
            Text(paymentMethod.label)
        }
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
    }
}

data class PaymentMethod(
    val method : PAYMENT_METHOD,
    val label : String,
    val icon: Int
)