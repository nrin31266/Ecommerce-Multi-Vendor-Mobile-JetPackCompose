package com.nrin31266.ecommercemultivendor.presentation.utils
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nrin31266.ecommercemultivendor.R

enum class MessageType {
    ERROR, SUCCESS, WARNING
}

@Composable
fun CustomMessageBox(
    message: String,
    type: MessageType,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true
) {
    val (icon, bgColor, iconTint) = when (type) {
        MessageType.ERROR -> Triple(Icons.Default.ErrorOutline, Color(0xFFFFE6E6), Color(0xFFCC0000))
        MessageType.SUCCESS -> Triple(Icons.Default.CheckCircle, Color(0xFFE6FFEA), Color(0xFF1E7F38))
        MessageType.WARNING -> Triple(Icons.Default.Warning, Color(0xFFFFF9E6), Color(0xFFE6A500))
    }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .background(bgColor, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
//            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if (showIcon) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
//                        modifier = Modifier.size(24.dp)
                    )
                }
                if (type == MessageType.ERROR) {
                    Spacer(Modifier.size(8.dp))
                    Icon(
                        modifier = Modifier.clickable {
                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(message))
                            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
                        }.size(20.dp),
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = ""
                    )
                }
            }
            Spacer(Modifier.size(8.dp))
            Column (
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = message,
                    color = iconTint,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 10,

                )
            }


        }
    }
}
