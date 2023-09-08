package com.rahmadina.ika.movieapp_kotlin.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rahmadina.ika.movieapp_kotlin.data.model.Favorite
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.utils.Constant.Companion.FAVORITE

class MovieTvRepositoryImpl(
    private val auth : FirebaseAuth,
    private val db : FirebaseFirestore)
    : MovieTvRepository {

    override fun addToFavorite(favorite: Favorite, response: (ApiResponse<String>) -> Unit) {
        val document = db.collection(FAVORITE).document(favorite.id)
        document.set(favorite)
            .addOnSuccessListener {
                response.invoke(
                    ApiResponse.Success("successfully add to favorite!!")
                )
            }
            .addOnFailureListener {
                response.invoke(
                    ApiResponse.Error(it.localizedMessage!!)
                )
            }
    }

    override fun removeFavorite(favorite: Favorite, response: (ApiResponse<String>) -> Unit) {
        db.collection(FAVORITE)
            .document(favorite.id)
            .delete()
            .addOnSuccessListener {
                response.invoke(
                    ApiResponse.Success("Removed from favorite!!")
                )
            }
            .addOnFailureListener {
                response.invoke(
                    ApiResponse.Error(it.message!!)
                )
            }
    }

    override fun getFavoriteUser(id: String, response: (ApiResponse<List<Favorite>>) -> Unit) {
        db.collection(FAVORITE)
            .whereEqualTo("idUser", id)
            .get()
            .addOnSuccessListener {
                val favUsers = arrayListOf<Favorite>()

                for (document in it){
                    val favUser = document.toObject(Favorite::class.java)
                    favUsers.add(favUser)
                }

                response.invoke(ApiResponse.Success(favUsers))
            }
            .addOnFailureListener {
                response.invoke(ApiResponse.Error(it.localizedMessage!!))
            }
    }

    override fun checkFav(id : String, response: (ApiResponse<List<Favorite>>) -> Unit) {
        db.collection(FAVORITE)
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener {
                val favs = arrayListOf<Favorite>()

                for (document in it){
                    val fav = document.toObject(Favorite::class.java)
                    favs.add(fav)
                }

                response.invoke(ApiResponse.Success(favs))
            }
            .addOnFailureListener {
                response.invoke(ApiResponse.Error(it.localizedMessage!!))
            }
    }
}