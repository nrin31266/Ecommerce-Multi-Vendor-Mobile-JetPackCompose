package com.nrin31266.ecommercemultivendor.domain.dto

import com.nrin31266.ecommercemultivendor.common.constant.USER_ROLE

data class UserDto(
    val id: Long,
//    val password: String,
    val email: String,
    val fullName: String,
    val mobile: String,
    val role: USER_ROLE,
    val pickupAddress: AddressDto?=null
)
