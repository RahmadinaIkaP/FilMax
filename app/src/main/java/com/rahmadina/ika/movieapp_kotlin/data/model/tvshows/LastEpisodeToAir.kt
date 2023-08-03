package com.rahmadina.ika.movieapp_kotlin.data.model.tvshows


import com.google.gson.annotations.SerializedName

data class LastEpisodeToAir(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("episode_number")
    val episodeNumber: Int,
    @SerializedName("production_code")
    val productionCode: String,
    @SerializedName("runtime")
    val runtime: Any,
    @SerializedName("season_number")
    val seasonNumber: Int,
    @SerializedName("show_id")
    val showId: Int,
    @SerializedName("still_path")
    val stillPath: Any
)