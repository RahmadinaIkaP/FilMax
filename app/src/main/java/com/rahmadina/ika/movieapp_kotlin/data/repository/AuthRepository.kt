package com.rahmadina.ika.movieapp_kotlin.data.repository

import com.rahmadina.ika.movieapp_kotlin.data.model.User
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse

interface AuthRepository {
    fun register(email : String, password : String, user: User, response: (ApiResponse<String>) -> Unit)
    fun login(email: String, password: String, response: (ApiResponse<String>) -> Unit)
    fun addUserToDatabase(id : String, user: User, response: (ApiResponse<String>) -> Unit)
    fun logout(response: () -> Unit)
    fun getUser(response: (ApiResponse<List<User>>) -> Unit)
}