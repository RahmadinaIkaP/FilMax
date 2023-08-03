package com.rahmadina.ika.movieapp_kotlin.data.model.movie


import com.google.gson.annotations.SerializedName

data class ResponsePopularMovie(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<com.rahmadina.ika.movieapp_kotlin.data.model.movie.ResultPopularMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)