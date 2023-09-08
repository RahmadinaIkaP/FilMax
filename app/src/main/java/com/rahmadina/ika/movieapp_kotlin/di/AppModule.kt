package com.rahmadina.ika.movieapp_kotlin.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rahmadina.ika.movieapp_kotlin.data.network.MovieEndpoint
import com.rahmadina.ika.movieapp_kotlin.data.repository.AuthRepository
import com.rahmadina.ika.movieapp_kotlin.data.repository.AuthRepositoryImpl
import com.rahmadina.ika.movieapp_kotlin.data.repository.MovieTvRepository
import com.rahmadina.ika.movieapp_kotlin.data.repository.MovieTvRepositoryImpl
import com.rahmadina.ika.movieapp_kotlin.utils.Constant.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val logging : HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit) : MovieEndpoint =
        retrofit.create(MovieEndpoint::class.java)

    @Provides
    @Singleton
    fun provideFirebaseFirestore() : FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth : FirebaseAuth, database : FirebaseFirestore) :
            AuthRepository = AuthRepositoryImpl(auth, database)

    @Provides
    @Singleton
    fun provideMovieTvRepository(auth : FirebaseAuth, database : FirebaseFirestore) :
            MovieTvRepository = MovieTvRepositoryImpl(auth, database)
}