package com.rahmadina.ika.movieapp_kotlin.data.model.search


import com.google.gson.annotations.SerializedName

data class ResultSearch(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("known_for_department")
    val knownForDepartment: String,
    @SerializedName("profile_path")
    val profilePath: String,
    @SerializedName("known_for")
    val knownFor: List<com.rahmadina.ika.movieapp_kotlin.data.model.search.KnownFor>
)