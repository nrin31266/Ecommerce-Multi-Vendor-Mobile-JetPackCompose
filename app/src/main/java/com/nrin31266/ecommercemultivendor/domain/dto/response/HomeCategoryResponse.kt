package com.nrin31266.ecommercemultivendor.domain.dto.response

import com.nrin31266.ecommercemultivendor.domain.dto.HomeCategoryDto

data class HomeCategoryResponse(
    val electronics : List<HomeCategoryDto> = emptyList(),
    val men : List<HomeCategoryDto> = emptyList(),
    val women : List<HomeCategoryDto> = emptyList(),
    val homeFurniture : List<HomeCategoryDto> = emptyList(),
)
