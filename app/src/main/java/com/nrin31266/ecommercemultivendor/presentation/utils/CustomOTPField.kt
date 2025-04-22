package com.nrin31266.ecommercemultivendor.presentation.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Replay
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomOTPField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "OTP",
    placeholder: String = "Enter",
    canResend: Boolean,
    onResend: () -> Unit,
    timeRemaining: Int? = null,
    modifier: Modifier = Modifier
) {
    CustomTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        leadingIcon = Icons.Default.Password,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        trailingIcon = {
            if (canResend) {
                IconButton (onClick = onResend) {
                    Icon(imageVector = Icons.Default.Replay, contentDescription = "Resend OTP")
                }
            } else {
                Text(
                    text = timeRemaining?.let { "$it s" } ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    )
}
