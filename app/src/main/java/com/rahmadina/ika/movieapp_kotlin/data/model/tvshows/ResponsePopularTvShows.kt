package com.rahmadina.ika.movieapp_kotlin.data.model.tvshows


import com.google.gson.annotations.SerializedName

data class ResponsePopularTvShows(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<com.rahmadina.ika.movieapp_kotlin.data.model.tvshows.ResultPopularTvShows>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)