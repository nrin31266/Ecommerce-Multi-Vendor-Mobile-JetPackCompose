package com.nrin31266.ecommercemultivendor.domain.dto.request

data class AuthRequest(
    val email: String,
    val role: String,
    val otp: String,
    val fullName: String,
)