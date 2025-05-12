package com.nrin31266.ecommercemultivendor

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nrin31266.ecommercemultivendor.presentation.nav.App
import com.nrin31266.ecommercemultivendor.ui.theme.EcommerceMultiVendorTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Nhận intent nếu app vừa được launch từ VNPAY
        handlePaymentRedirect(intent)

        setContent {
            EcommerceMultiVendorTheme {
                App()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handlePaymentRedirect(intent)
    }

    private fun handlePaymentRedirect(intent: Intent?) {
        val data = intent?.data ?: return
        if (data.scheme == "yourapp" && data.host == "payment-result") {
            val status = data.getQueryParameter("status")
            val paymentId = data.getQueryParameter("paymentId")

            val isSuccess = status == "00"

            // TODO: bạn có thể lưu kết quả này vào ViewModel, SavedStateHandle hoặc một singleton
            // hoặc broadcast, v.v. để App Composable xử lý sau

            // Ví dụ nhanh (xấu nhưng minh họa): lưu tạm vào SharedPreferences
            val prefs = getSharedPreferences("vnpay", MODE_PRIVATE)
            prefs.edit()
                .putBoolean("payment_result_pending", true)
                .putBoolean("payment_success", isSuccess)
                .apply()
        }
    }
}


