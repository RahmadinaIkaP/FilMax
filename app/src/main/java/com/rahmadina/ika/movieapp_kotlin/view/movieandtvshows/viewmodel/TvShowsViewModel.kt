package com.rahmadina.ika.movieapp_kotlin.view.movieandtvshows.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahmadina.ika.movieapp_kotlin.data.model.tvshows.ResponseDetailTvShows
import com.rahmadina.ika.movieapp_kotlin.data.model.tvshows.ResponsePopularTvShows
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
class TvShowsViewModel @Inject constructor(private val api : MovieEndpoint) : ViewModel() {
    private val popularTvLD : MutableLiveData<ApiResponse<ResponsePopularTvShows>> = MutableLiveData()
    private val detailTvLD : MutableLiveData<ApiResponse<ResponseDetailTvShows>> = MutableLiveData()

    fun getPopularTvObserver() : MutableLiveData<ApiResponse<ResponsePopularTvShows>> = popularTvLD
    fun getDetailTvObserver() : MutableLiveData<ApiResponse<ResponseDetailTvShows>> = detailTvLD

    fun getPopularTvShows(){
        popularTvLD.postValue(ApiResponse.Loading())

        api.getPopularTvShows(TOKEN).enqueue(object :
            Callback<ResponsePopularTvShows> {
            override fun onResponse(
                call: Call<ResponsePopularTvShows>,
                response: Response<ResponsePopularTvShows>
            ) {
                if (response.isSuccessful){
                    val data = response.body()

                    data?.let {
                        popularTvLD.postValue(ApiResponse.Success(it))
                    }
                }else{
                    try {
                        response.errorBody()?.let {
                            val jObject = JSONObject(it.string()).getString("status_message")
                            popularTvLD.postValue(ApiResponse.Error(jObject))
                        }
                    }catch (e: Exception){
                        popularTvLD.postValue(ApiResponse.Error("${e.message}"))
                    }
                }
            }

            override fun onFailure(call: Call<ResponsePopularTvShows>, t: Throwable) {
                popularTvLD.postValue(ApiResponse.Error("${t.message}"))
            }

        })
    }

    fun getDetailTvShows(id : Int){
        detailTvLD.postValue(ApiResponse.Loading())

        api.getDetailTvShow(TOKEN, id).enqueue(object : Callback<ResponseDetailTvShows>{
            override fun onResponse(
                call: Call<ResponseDetailTvShows>,
                response: Response<ResponseDetailTvShows>
            ) {

                if (response.isSuccessful){
                    val data = response.body()

                    data?.let {
                        detailTvLD.postValue(ApiResponse.Success(it))
                    }
                }else{
                    try {
                        response.errorBody()?.let {
                            val jObject = JSONObject(it.string()).getString("status_message")
                            detailTvLD.postValue(ApiResponse.Error(jObject))
                        }
                    }catch (e: Exception){
                        detailTvLD.postValue(ApiResponse.Error("${e.message}"))
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDetailTvShows>, t: Throwable) {
                detailTvLD.postValue(ApiResponse.Error("${t.message}"))
            }

        })
    }
}