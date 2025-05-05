package com.nrin31266.ecommercemultivendor.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CustomTopBar(
    title: String? = null,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    backgroundColor: Color = Color.Transparent,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    actionIcon: ImageVector? = null,
    onActionClick: (() -> Unit)? = null,
    extraContent: (@Composable () -> Unit)? = null,
    content : (@Composable () -> Unit)? = null,
    customAction : (@Composable () -> Unit)? = null,
    hasDivider: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {

            if (onBackClick != null) {
                IconButton(
                    onClick = onBackClick,

                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = titleColor
                    )
                }
            }



            Column (modifier = Modifier.weight(1f)){
                if(title != null){
                    Text(
                        text = title,
                        color = titleColor,
                        fontSize = 20.sp,

                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if(content != null){
                    content()
                }
            }

            if(customAction!=null){
                customAction()
            }else
            if (actionIcon != null && onActionClick != null) {
                IconButton(
                    onClick = onActionClick,

                ) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = "Action",
                        tint = titleColor
                    )
                }
            }
        }


        if (extraContent != null) {
            extraContent()
        }
        if (hasDivider) {
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
            )
        }
    }
}
