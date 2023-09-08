package com.rahmadina.ika.movieapp_kotlin.view.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahmadina.ika.movieapp_kotlin.data.model.User
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private var registerLiveData : MutableLiveData<ApiResponse<String>> = MutableLiveData()
    private var loginLiveData : MutableLiveData<ApiResponse<String>> = MutableLiveData()
    private var getUserLiveData : MutableLiveData<ApiResponse<List<User>>> = MutableLiveData()

    fun registerObserver() : MutableLiveData<ApiResponse<String>> = registerLiveData
    fun loginObserver() : MutableLiveData<ApiResponse<String>> = loginLiveData
    fun getUserObserver() : MutableLiveData<ApiResponse<List<User>>> = getUserLiveData

    fun register(email : String, password : String, user: User){
        registerLiveData.value = ApiResponse.Loading()
        repository.register(email, password, user){
            registerLiveData.value = it
        }
    }

    fun login(email: String, password: String){
        loginLiveData.value = ApiResponse.Loading()
        repository.login(email, password){
            loginLiveData.value = it
        }
    }

    fun logout(result: () -> Unit){
        repository.logout(result)
    }

    fun getUser(){
        getUserLiveData.value = ApiResponse.Loading()
        repository.getUser{
            getUserLiveData.value = it
        }
    }
}