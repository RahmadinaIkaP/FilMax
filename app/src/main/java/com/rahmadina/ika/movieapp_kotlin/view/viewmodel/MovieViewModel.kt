package com.rahmadina.ika.movieapp_kotlin.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.ResponseDetailMovie
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.ResponsePopularMovie
import com.rahmadina.ika.movieapp_kotlin.data.model.movie.trending.ResponseTrendingMovies
import com.rahmadina.ika.movieapp_kotlin.data.model.search.ResponseSearch
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.data.network.MovieEndpoint
import com.rahmadina.ika.movieapp_kotlin.utils.Constant.Companion.TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val api : MovieEndpoint) : ViewModel() {
    private val liveDataSearch : MutableLiveData<ApiResponse<ResponseSearch>> = MutableLiveData()
    private val popularMovieLD : MutableLiveData<ApiResponse<ResponsePopularMovie>> = MutableLiveData()
    private val detailMovieLD : MutableLiveData<ApiResponse<ResponseDetailMovie>> = MutableLiveData()
    private val trendingMovieLD : MutableLiveData<ApiResponse<ResponseTrendingMovies>> = MutableLiveData()

    fun getSearchObserver() : MutableLiveData<ApiResponse<ResponseSearch>> = liveDataSearch
    fun getPopularMovieObserver() : MutableLiveData<ApiResponse<ResponsePopularMovie>> = popularMovieLD
    fun getDetailMovieObserver() : MutableLiveData<ApiResponse<ResponseDetailMovie>> = detailMovieLD
    fun getTrendingMovieObserver() : MutableLiveData<ApiResponse<ResponseTrendingMovies>> = trendingMovieLD

    fun getSearchResult(keyword : String){
        liveDataSearch.postValue(ApiResponse.Loading())

        api.getSearchResult(TOKEN, keyword).enqueue(object : Callback<ResponseSearch>{
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                if (response.isSuccessful){
                    val data = response.body()

                    data?.let {
                        liveDataSearch.postValue(ApiResponse.Success(it))
                    }
                }else{
                    try {
                        response.errorBody()?.let {
                            val jObject = JSONObject(it.string()).getString("status_message")
                            liveDataSearch.postValue(ApiResponse.Error(jObject))
                        }
                    }catch (e: Exception){
                        liveDataSearch.postValue(ApiResponse.Error("${e.message}"))
                    }
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                liveDataSearch.postValue(ApiResponse.Error("${t.message}"))
            }

        })
    }

    fun getPopularMovie(){
        popularMovieLD.postValue(ApiResponse.Loading())

        api.getPopularMovie(TOKEN).enqueue(object : Callback<ResponsePopularMovie>{
            override fun onResponse(
                call: Call<ResponsePopularMovie>,
                response: Response<ResponsePopularMovie>
            ) {
                if (response.isSuccessful){
                    val data = response.body()

                    data?.let {
                        popularMovieLD.postValue(ApiResponse.Success(it))
                    }
                }else{
                    try {
                        response.errorBody()?.let {
                            val jObject = JSONObject(it.string()).getString("status_message")
                            popularMovieLD.postValue(ApiResponse.Error(jObject))
                        }
                    }catch (e: Exception){
                        popularMovieLD.postValue(ApiResponse.Error("${e.message}"))
                    }
                }
            }

            override fun onFailure(call: Call<ResponsePopularMovie>, t: Throwable) {
                popularMovieLD.postValue(ApiResponse.Error("${t.message}"))
            }

        })
    }

    fun getTrendingMovie(){
        trendingMovieLD.postValue(ApiResponse.Loading())

        api.getTrendingMovies(TOKEN).enqueue(object : Callback<ResponseTrendingMovies>{
            override fun onResponse(
                call: Call<ResponseTrendingMovies>,
                response: Response<ResponseTrendingMovies>
            ) {
                if (response.isSuccessful){
                    val data = response.body()

                    data?.let {
                        trendingMovieLD.postValue(ApiResponse.Success(it))
                    }
                }else{
                    try {
                        response.errorBody()?.let {
                            val jObject = JSONObject(it.string()).getString("status_message")
                            trendingMovieLD.postValue(ApiResponse.Error(jObject))
                        }
                    }catch (e: Exception){
                        trendingMovieLD.postValue(ApiResponse.Error("${e.message}"))
                    }
                }
            }

            override fun onFailure(call: Call<ResponseTrendingMovies>, t: Throwable) {
                trendingMovieLD.postValue(ApiResponse.Error("${t.message}"))
            }

        })
    }

    fun getDetailMovie(id : Int){
        detailMovieLD.postValue(ApiResponse.Loading())

        api.getDetailMovie(TOKEN, id).enqueue(object : Callback<ResponseDetailMovie>{
            override fun onResponse(
                call: Call<ResponseDetailMovie>,
                response: Response<ResponseDetailMovie>
            ) {

                if (response.isSuccessful){
                    val data = response.body()

                    data?.let {
                        detailMovieLD.postValue(ApiResponse.Success(it))
                    }
                }else{
                    try {
                        response.errorBody()?.let {
                            val jObject = JSONObject(it.string()).getString("status_message")
                            detailMovieLD.postValue(ApiResponse.Error(jObject))
                        }
                    }catch (e: Exception){
                        detailMovieLD.postValue(ApiResponse.Error("${e.message}"))
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDetailMovie>, t: Throwable) {
                detailMovieLD.postValue(ApiResponse.Error("${t.message}"))
            }

        })
    }
}