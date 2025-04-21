package com.nrin31266.ecommercemultivendor.domain.dto.response

import com.squareup.moshi.JsonClass

//public class ApiResponse<T> {
//    String message;
//    @Builder.Default
//    int code = 1000;
//    @Builder.Default
//    LocalDateTime timestamp = LocalDateTime.now();
//    T data;
//}


data class ApiResponse<out T>(
    val message: String?=null,
    val code: Int?=null,
    val timestamp: String?=null,
    val data: T?=null
)

data class ApiResponseNoData(
    val message: String?=null,
    val code: Int?=null,
    val timestamp: String?=null,
)