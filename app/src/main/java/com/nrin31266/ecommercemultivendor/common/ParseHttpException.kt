package com.nrin31266.ecommercemultivendor.common

import com.nrin31266.ecommercemultivendor.domain.dto.response.ApiResponseNoData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.HttpException

fun HttpException.toReadableErrorMoshi(): String {
    return try {
        val errorBody = response()?.errorBody()?.string()
        println("Error Body: $errorBody")
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(ApiResponseNoData::class.java)
        val error = adapter.fromJson(errorBody ?: "")
        error?.message ?: "HTTP ${code()}"
    } catch (e: Exception) {
        "Unknown error: HTTP ${code()} ${message()}"
    }
}
