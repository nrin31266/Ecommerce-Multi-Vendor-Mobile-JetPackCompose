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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
enum class ButtonType {
    FILLED, OUTLINED, TEXT
}


enum class ButtonSize(val height: Dp, val textSize: TextUnit, val iconSize: Dp) {
    SMALL(34.dp, 14.sp, 10.dp),
    MEDIUM(48.dp, 14.sp, 18.dp),
    LARGE(56.dp, 16.sp, 20.dp),
    EXTRALARGE(64.dp, 18.sp, 24.dp)
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
    size: ButtonSize = ButtonSize.MEDIUM, // Mặc định là MEDIUM
    borderColor: Color = backgroundColor
) {
    val shape = RoundedCornerShape(cornerRadius)

    val content: @Composable RowScope.() -> Unit = {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(size.iconSize),
                color = contentColor,
                strokeCap = StrokeCap.Round,
                strokeWidth = 2.dp
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(size.iconSize)
            )
        }
        Text(
            text = text,
            fontSize = size.textSize,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }

    val buttonModifier = modifier.height(size.height)

    when (type) {
        ButtonType.FILLED -> Button(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
            ),
            elevation = elevation,
            content = content
        )

        ButtonType.OUTLINED -> OutlinedButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = backgroundColor
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    listOf(borderColor, borderColor)
                )
            ),
            content = content
        )

        ButtonType.TEXT -> TextButton(
            onClick = onClick,
            modifier = buttonModifier,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.textButtonColors(
                contentColor = backgroundColor
            ),
            content = content
        )
    }
}
