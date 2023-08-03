package com.rahmadina.ika.movieapp_kotlin.data.model.search


import com.google.gson.annotations.SerializedName

data class ResponseSearch(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<com.rahmadina.ika.movieapp_kotlin.data.model.search.ResultSearch>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)