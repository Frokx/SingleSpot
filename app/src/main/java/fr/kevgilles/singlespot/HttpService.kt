package fr.kevgilles.singlespot

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface HttpService {

    @GET("points.json")
    fun getAllLocationPoints(): Call<String>

    @GET("{id}")
    fun getLatLong(@Path("id")id: String): Call<String>
}