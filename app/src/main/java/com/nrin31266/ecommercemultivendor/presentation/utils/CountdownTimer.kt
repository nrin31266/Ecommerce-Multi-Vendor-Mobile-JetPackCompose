package com.nrin31266.ecommercemultivendor.presentation.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.nrin31266.ecommercemultivendor.R
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun CountdownTimer(expiryDate: String) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    val zoneId = ZoneId.of("Asia/Ho_Chi_Minh")

    val expiryMillis = remember(expiryDate) {
        try {
            val localDateTime = LocalDateTime.parse(expiryDate, formatter)
            localDateTime.atZone(zoneId).toInstant().toEpochMilli()
        } catch (e: Exception) {
            null
        }
    }

    var remainingTime by remember { mutableLongStateOf(getRemainingTime(expiryMillis)) }

    LaunchedEffect(expiryMillis) {
        while (remainingTime > 0) {
            delay(1000L)
            remainingTime = getRemainingTime(expiryMillis)
        }
    }

    if (expiryMillis == null) {
        Text("Invalid date", color = Color.Red)
    } else if (remainingTime <= 0) {
        Text("Expired", color = Color.Red)
    } else {
        val hours = (remainingTime / (1000 * 60 * 60)) % 24
        val minutes = (remainingTime / (1000 * 60)) % 60
        val seconds = (remainingTime / 1000) % 60

        Text(
            text = String.format("Remaining: %02d:%02d:%02d", hours, minutes, seconds),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.warning_orange)
        )
    }
}

fun getRemainingTime(expiryMillis: Long?): Long {
    val now = System.currentTimeMillis()
    return if (expiryMillis != null) expiryMillis - now else 0
}
