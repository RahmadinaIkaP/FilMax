package com.rahmadina.ika.movieapp_kotlin.data.repository

import com.rahmadina.ika.movieapp_kotlin.data.model.Favorite
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse

interface MovieTvRepository {
    fun addToFavorite(favorite : Favorite, response: (ApiResponse<String>) -> Unit)
    fun removeFavorite(favorite: Favorite, response: (ApiResponse<String>) -> Unit)
    fun getFavoriteUser(id : String, response: (ApiResponse<List<Favorite>>) -> Unit)
    fun checkFav(id : String, response: (ApiResponse<List<Favorite>>) -> Unit)
}