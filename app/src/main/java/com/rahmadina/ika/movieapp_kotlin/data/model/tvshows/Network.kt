package com.rahmadina.ika.movieapp_kotlin.data.model.tvshows


import com.google.gson.annotations.SerializedName

data class Network(
    @SerializedName("id")
    val id: Int,
    @SerializedName("logo_path")
    val logoPath: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String
)