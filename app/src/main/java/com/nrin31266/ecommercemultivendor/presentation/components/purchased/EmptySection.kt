package com.nrin31266.ecommercemultivendor.presentation.components.purchased

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun EmptySection(
    title: String?=null,

) {
   Box(
       modifier = Modifier.height(750.dp).fillMaxWidth(),
       contentAlignment = androidx.compose.ui.Alignment.Center,
   ){
       Text(
           "No data to display"
       )
   }
}
