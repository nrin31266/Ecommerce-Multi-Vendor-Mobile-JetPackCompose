package com.nrin31266.ecommercemultivendor.domain.dto

import com.nrin31266.ecommercemultivendor.domain.dto.response.ShopCartGroupResponse

//Long id;
//User user;
////    Long totalSellingPrice = 0L;
////    int totalItems = 0;
////    Long totalMrpPrice = 0L;
////    int discount;
//String couponCode;
//List<ShopCartGroupResponse> groups;
data class CartDto(
    val id: Long,
    val user: UserDto,
    val couponCode: String? = null,
    val groups: List<ShopCartGroupResponse>
)