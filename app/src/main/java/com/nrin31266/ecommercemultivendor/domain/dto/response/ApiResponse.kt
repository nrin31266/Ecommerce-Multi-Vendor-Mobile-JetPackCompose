package com.nrin31266.ecommercemultivendor.domain.dto.response

//public class ApiResponse<T> {
//    String message;
//    @Builder.Default
//    int code = 1000;
//    @Builder.Default
//    LocalDateTime timestamp = LocalDateTime.now();
//    T data;
//}
data class ApiResponse<out T>(
    val message: String,
    val code: Int,
    val timestamp: String,
    val data: T
)