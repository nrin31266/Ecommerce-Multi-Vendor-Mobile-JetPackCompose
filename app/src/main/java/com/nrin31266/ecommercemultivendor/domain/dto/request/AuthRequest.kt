package com.nrin31266.ecommercemultivendor.domain.dto.request

import com.nrin31266.ecommercemultivendor.common.constant.USER_ROLE

data class AuthRequest(
    val email: String?=null,
    val role: USER_ROLE= USER_ROLE.ROLE_CUSTOMER,
    val otp: String?=null,
    val fullName: String?=null,

)