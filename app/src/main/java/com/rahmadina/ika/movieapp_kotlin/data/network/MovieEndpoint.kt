package com.rahmadina.ika.movieapp_kotlin.data.network

import com.rahmadina.ika.movieapp_kotlin.data.model.movie.ResponseDetailMovie
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.ResponsePopularMovie
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.trending.ResponseTrendingMovies
import com.rahmadina.ika.movieapp_kotlin.data.model.tvshows.ResponsePopularTvShows
import com.rahmadina.ika.movieapp_kotlin.data.model.search.ResponseSearch
import com.rahmadina.ika.movieapp_kotlin.data.model.tvshows.ResponseDetailTvShows
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieEndpoint {

    // fitur search
    @GET("3/search/multi")
    fun getSearchResult(
        @Header("Authorization") token : String,
        @Query("query") keyword : String
    ) : Call<ResponseSearch>

    // get popular movie
    @GET("3/movie/popular?language=en&page=3")
    fun getPopularMovie(
        @Header("Authorization") token : String
    ) : Call<ResponsePopularMovie>

    // get detail movie
    @GET("3/movie/{movie_id}")
    fun getDetailMovie(
        @Header("Authorization") token : String,
        @Path("movie_id") id: Int
    ) : Call<ResponseDetailMovie>

    // get popular tv
    @GET("3/tv/popular?language=en&page=1")
    fun getPopularTvShows(
        @Header("Authorization") token : String
    ) : Call<ResponsePopularTvShows>

    // get detail tv shows
    @GET("3/tv/{series_id}")
    fun getDetailTvShow(
        @Header("Authorization") token : String,
        @Path("series_id") id: Int
    ) : Call<ResponseDetailTvShows>

    // get all trending movies
    @GET("3/trending/movie/week")
    fun getTrendingMovies(
        @Header("Authorization") token : String
    ) : Call<ResponseTrendingMovies>
}