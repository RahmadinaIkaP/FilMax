package com.rahmadina.ika.movieapp_kotlin.data.model.movie.trending


import com.google.gson.annotations.SerializedName

data class ResponseTrendingMovies(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ResultTrendingMovies>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)