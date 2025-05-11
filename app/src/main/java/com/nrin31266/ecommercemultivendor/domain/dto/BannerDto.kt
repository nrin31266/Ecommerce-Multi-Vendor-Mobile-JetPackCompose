package com.nrin31266.ecommercemultivendor.domain.dto

import com.nrin31266.ecommercemultivendor.common.constant.BANNER_TARGET_TYPE

data class BannerDto(
    val id: Int,
    val imageUrl:String,
    val title: String,
    val targetType: BANNER_TARGET_TYPE,
    val target: String,
    val active: Boolean,
    val startDate: String,
    val endDate: String
)
