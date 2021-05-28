package com.example.weekseven.api

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.nio.file.attribute.AclEntry.newBuilder
import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 *Links the application to the server binding the endpoint and baseUrl provided and formats how the data is to be submitted to the server
 * */

interface ImageAPI {
    @Multipart
    @POST("upload")
    fun uploadImage(
            @Part image: MultipartBody.Part,
            @Part("multipart/form-data") body: RequestBody
    ): Call<ImageResponse>

    companion object {
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .build()


        operator fun invoke(): ImageAPI {
            return Retrofit.Builder()
                    .baseUrl("https://darot-image-upload-service.herokuapp.com/api/v1/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ImageAPI::class.java)
        }
    }

}