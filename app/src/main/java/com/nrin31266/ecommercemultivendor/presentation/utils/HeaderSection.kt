package com.nrin31266.ecommercemultivendor.presentation.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    icon: ImageVector?=null,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    title: String?=null,
    titleSize: TextUnit = 16.sp,
    actionName: String = "View",
    actionColor: Color = Color.Gray,
    actionIcon: ImageVector = Icons.Default.ArrowForwardIos,
    actionIconSize: Dp = 12.dp,
    onActionClick: (() -> Unit)? = null,
    extraTitle: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
           if(icon!=null){
               Icon(
                   imageVector = icon,
                   contentDescription = null,
                   tint = iconTint
               )
           }
            if(title!=null){
                Text(
                    text = title,
                    fontSize = titleSize
                )
            }else if(extraTitle != null) {
                extraTitle()
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 6.dp)
                .then(
                    if (onActionClick != null) Modifier.clickable { onActionClick() } else Modifier
                )
        ) {
            Text(
                text = actionName,
                color = actionColor
            )
            Icon(
                imageVector = actionIcon,
                contentDescription = null,
                tint = actionColor,
                modifier = Modifier.size(actionIconSize)
            )
        }
    }
}
