package com.example.androidproyect19022025.data.api

import com.example.androidproyect19022025.data.Pokemon
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonInterfaz {
    @GET("api/v2/pokemon/{id}")
    suspend fun getPokemonInfo(@Path("id") id: Int): Pokemon
}