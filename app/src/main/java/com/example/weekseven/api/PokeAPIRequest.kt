package com.example.weekseven.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


/**
 *Fetches images from the given api after creating the Retrofit call and and controlling the amount to be displayed
 * */

const val BASE_URL = "https://pokeapi.co/api/v2/"

val myApi = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(PokeAPIRequest::class.java)

interface PokeAPIRequest {

    @GET("pokemon?limit=50")
    suspend fun getPokeBasic(): PokeApiJSON

    @GET("pokemon/{id}")
    suspend fun getPokeDetails(
        @Path("id") id: String
    ): PokeFullJSON


}