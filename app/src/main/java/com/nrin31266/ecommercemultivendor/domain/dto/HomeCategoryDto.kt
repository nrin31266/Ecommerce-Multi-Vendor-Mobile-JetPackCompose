package com.nrin31266.ecommercemultivendor.domain.dto

import com.nrin31266.ecommercemultivendor.common.constant.HOME_CATEGORY_SECTION

data class HomeCategoryDto(
    val id: Long,
    val name: String,
    val image: String,
    val categoryIds: String,
    val homeCategorySection: HOME_CATEGORY_SECTION
)
