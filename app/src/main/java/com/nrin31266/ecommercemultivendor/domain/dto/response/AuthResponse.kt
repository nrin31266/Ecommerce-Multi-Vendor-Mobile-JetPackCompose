package com.nrin31266.ecommercemultivendor.domain.dto.response

import com.nrin31266.ecommercemultivendor.common.constant.USER_ROLE

data class AuthResponse(
    val jwt:String,
    val message:String,
    val role: USER_ROLE
)
