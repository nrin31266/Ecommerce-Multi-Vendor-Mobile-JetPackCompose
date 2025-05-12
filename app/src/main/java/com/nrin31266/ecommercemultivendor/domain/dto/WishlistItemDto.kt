package com.nrin31266.ecommercemultivendor.domain.dto

data class WishlistItemDto(
    val id: Long,
    val user: UserDto,
    val product: ProductDto,
    val addedAt: String
)