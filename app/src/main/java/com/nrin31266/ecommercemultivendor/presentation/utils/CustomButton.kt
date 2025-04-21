package com.nrin31266.ecommercemultivendor.presentation.utils
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nrin31266.ecommercemultivendor.R

enum class ButtonType {
    FILLED, OUTLINED, TEXT
}

@Composable
fun CustomButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    cornerRadius: Dp = 8.dp,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(5.dp),
    loading: Boolean = false,
    type: ButtonType = ButtonType.FILLED,
    borderColor: Color = backgroundColor
) {
    val shape = RoundedCornerShape(cornerRadius)

    val content: @Composable RowScope.() -> Unit = {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(end = 8.dp).size(18.dp),
                color = contentColor,
                strokeCap = StrokeCap.Round,
                strokeWidth = 2.dp
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)

            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }

    when (type) {
        ButtonType.FILLED -> Button(
            onClick = onClick,
            modifier = modifier.height(48.dp),
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
            ),
            elevation = elevation,
            content = content
        )

        ButtonType.OUTLINED -> OutlinedButton (
            onClick = onClick,
            modifier = modifier.height(48.dp),
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = backgroundColor
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.Brush.linearGradient(listOf(borderColor, borderColor))
            ),
            content = content
        )

        ButtonType.TEXT -> TextButton (
            onClick = onClick,
            modifier = modifier.height(48.dp),
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.textButtonColors(
                contentColor = backgroundColor
            ),
            content = content
        )
    }
}
