package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.*

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

interface AsteroidApiService {
    @GET ( "neo/rest/v1/feed?")
    fun getProperties(@Query("start_date") date: String,
                      @Query("api_key") key: String): Call<String>
}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}