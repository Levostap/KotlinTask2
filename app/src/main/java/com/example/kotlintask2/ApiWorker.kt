package com.example.kotlintask2

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.kotlintask2.data.NekoResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface Api {
    @GET("{tag}")
    suspend fun getPicRandom(
        @Path("tag") tag : String, @Query("amount") amount : Int
    ) : NekoResponse
    @GET("search")
    suspend fun searchPic(
        @Query("query") query : String, @Query("type") type : Int, @Query("amount") amount : Int
    ) : NekoResponse
}

class ApiWorker {
    fun createApi(): Api {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(Api::class.java)
    }
    companion object{
        const val TAG = "neko"
        const val API_URL = "https://nekos.best/api/v2/"
        const val ITEMS_PER_PAGE = 14
    }
}