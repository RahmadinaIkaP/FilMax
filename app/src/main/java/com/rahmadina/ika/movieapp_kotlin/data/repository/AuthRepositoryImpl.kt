package com.rahmadina.ika.movieapp_kotlin.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rahmadina.ika.movieapp_kotlin.data.model.User
import com.rahmadina.ika.movieapp_kotlin.data.network.ApiResponse
import com.rahmadina.ika.movieapp_kotlin.utils.Constant.Companion.USER

class AuthRepositoryImpl(
    private val auth : FirebaseAuth,
    private val db : FirebaseFirestore
) : AuthRepository {
    override fun register(
        email: String,
        password: String,
        user: User,
        response: (ApiResponse<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.isSuccessful){
                    auth.currentUser?.let {
                        addUserToDatabase(it.uid, user){ responses ->
                            when(responses){
                                is ApiResponse.Success -> {
                                    response.invoke(ApiResponse.Success("Register success!"))
                                }
                                is ApiResponse.Loading -> {}
                                is ApiResponse.Error -> {
                                    response.invoke(ApiResponse.Error(responses.msg.toString()))
                                }
                            }
                        }
                    }
                }else{
                    response.invoke(ApiResponse.Error("Register failed!!"))
                }
            }
            .addOnFailureListener{
                response.invoke(ApiResponse.Error(it.localizedMessage!!))
            }
    }

    override fun login(email: String, password: String, response: (ApiResponse<String>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.isSuccessful){
                    getUser{ result2 ->
                        when(result2){
                            is ApiResponse.Success -> {
                                response.invoke(ApiResponse.Success("Login success!!"))
                            }
                            is ApiResponse.Error -> {
                                response.invoke(ApiResponse.Error(result2.msg.toString()))
                            }
                            is ApiResponse.Loading -> {}
                        }
                    }
                }
            }
            .addOnFailureListener {
                response.invoke(ApiResponse.Error("Login failed"))
            }
    }

    override fun addUserToDatabase(
        id: String,
        user: User,
        response: (ApiResponse<String>) -> Unit
    ) {
        val document = db.collection(USER).document(id)
        user.id = document.id
        document.set(user)
            .addOnSuccessListener {
                response.invoke(ApiResponse.Success("Successfully add data to database"))
            }
            .addOnFailureListener {
                response.invoke(ApiResponse.Error(it.localizedMessage!!))
            }
    }

    override fun logout(response: () -> Unit) {
        auth.signOut()
        response.invoke()
    }

    override fun getUser(response: (ApiResponse<List<User>>) -> Unit) {
        db.collection(USER)
            .whereEqualTo("id", auth.currentUser?.uid)
            .get()
            .addOnSuccessListener {
                val users = arrayListOf<User>()

                for (document in it){
                    val user = document.toObject(User::class.java)
                    users.add(user)
                }

                response.invoke(ApiResponse.Success(users))
            }
            .addOnFailureListener {
                response.invoke(ApiResponse.Error(it.localizedMessage!!))
            }
    }

}