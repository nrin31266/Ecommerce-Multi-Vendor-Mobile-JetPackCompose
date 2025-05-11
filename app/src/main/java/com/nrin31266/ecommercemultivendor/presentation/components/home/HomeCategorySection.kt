package com.nrin31266.ecommercemultivendor.presentation.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nrin31266.ecommercemultivendor.domain.dto.HomeCategoryDto
import com.nrin31266.ecommercemultivendor.presentation.nav.CustomerRoutes


@Composable
fun HomeCategorySection(
    homeCategories: List<HomeCategoryDto>,
    title: String = "Home Categories",
    navController: NavController
) {
    Column (
        modifier = Modifier.padding(8.dp)
    ){
        Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            items(homeCategories){
                HomeCategoryItem(it, {
                    navController.navigate(CustomerRoutes.ProductsScreen.withQuery(
                        category = it.categoryIds
                    ))
                })
            }
        }
    }
}