package com.coba.cermatiproject

import com.coba.cermatiproject.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class NetworkConfig {// set interceptor

fun getInterceptor() : OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    return  okHttpClient
    }

    fun getRetrofit() : Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.github.com/search/")
        .client(getInterceptor())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
    fun getService() = getRetrofit().create(Users::class.java)}

interface Users {
    @GET("users")
    fun getUsers(@Query("page") page:String, @Query("q") query:String): Call<User>
}