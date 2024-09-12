package com.example.tech_task.api
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterRequests {

    @GET("character/")
    suspend fun getCharacters(
        @Query("name") name: String?,
        @Query("page") page: Int? = null,): Response<RickAndMortyInfo>



}