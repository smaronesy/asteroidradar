package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

interface AsteroidApiService {
    @GET ( "neo/rest/v1/feed?start_date=2022-03-30&api_key=uLiVb2NgW8PhwHOQgG7UDPjOYnFwFcNIhzyHg4JQ")
    fun getProperties():
            Call<String>
}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}