package com.rahmadina.ika.movieapp_kotlin.view.profile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahmadina.ika.movieapp_kotlin.data.model.Favorite
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.data.repository.MovieTvRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: MovieTvRepository) : ViewModel() {
    private var addFavoriteLiveData : MutableLiveData<ApiResponse<String>> = MutableLiveData()
    private var removeFavoriteLiveData : MutableLiveData<ApiResponse<String>> = MutableLiveData()
    private var getFavUserLiveData : MutableLiveData<ApiResponse<List<Favorite>>> = MutableLiveData()
    private var checkFavLiveData : MutableLiveData<ApiResponse<List<Favorite>>> = MutableLiveData()

    fun addFavObserver() : MutableLiveData<ApiResponse<String>> = addFavoriteLiveData
    fun removeFavObserver() : MutableLiveData<ApiResponse<String>> = removeFavoriteLiveData
    fun getFavUserObserver() : MutableLiveData<ApiResponse<List<Favorite>>> = getFavUserLiveData
    fun checkFavObserver() : MutableLiveData<ApiResponse<List<Favorite>>> = checkFavLiveData

    fun addToFav(favorite: Favorite){
        addFavoriteLiveData.value = ApiResponse.Loading()
        repository.addToFavorite(favorite){
            addFavoriteLiveData.value = it
        }
    }

    fun removeFav(favorite: Favorite){
        addFavoriteLiveData.value = ApiResponse.Loading()
        repository.removeFavorite(favorite){
            removeFavoriteLiveData.value = it
        }
    }

    fun getFavUser(id : String){
        getFavUserLiveData.value = ApiResponse.Loading()
        repository.getFavoriteUser(id) {
            getFavUserLiveData.value = it
        }
    }

    fun checkFav(id : String){
        checkFavLiveData.value = ApiResponse.Loading()
        repository.checkFav(id){
            checkFavLiveData.value = it
        }
    }
}