package com.nrin31266.ecommercemultivendor.domain.dto.response

import com.nrin31266.ecommercemultivendor.domain.dto.CartItemDto
import com.nrin31266.ecommercemultivendor.domain.dto.SellerDto

//Seller seller;
//Set<CartItem> cartItems;
data class ShopCartGroupResponse(
    val seller: SellerDto,
    val cartItems: Set<CartItemDto>
)