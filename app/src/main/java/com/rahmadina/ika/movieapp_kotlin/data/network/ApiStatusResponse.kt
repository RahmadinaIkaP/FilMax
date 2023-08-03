package com.rahmadina.ika.movieapp_kotlin.data.network

sealed class ApiResponse<T>(val data: T? = null, val msg: String? = null) {
    class Success<T>(data: T) : ApiResponse<T>(data)
    class Loading<T>(data: T? = null) : ApiResponse<T>(data)
    class Error<T>(msg: String, data: T? = null) : ApiResponse<T>(data, msg)
}
