package com.example.androidproyect19022025.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ObjectApiClient {
    private val retrofit2 = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    //Con esto conseguimos que la variable pueda usar la interfaz sin necesidad de crear una instancia de la interfaz y demas
    val apiClient = retrofit2.create(PokemonInterfaz::class.java)

}