package com.example.androidproyect19022025.data

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("name") val nombre: String,
    @SerializedName("sprites") val pokemonSprite: PokemonSprite,
    @SerializedName("types") val tipos: ArrayList<Tipo>,
    @SerializedName("abilities") val habilidades: ArrayList<Habilidad>
)

data class Habilidad(
    @SerializedName("ability") val habilidad: NombreHabilidad
)
data class NombreHabilidad(
    @SerializedName("name") val nombreHabilidad: String
)

data class PokemonSprite(
    @SerializedName("versions") val versionJuego: Version
)

data class Version(
    @SerializedName("generation-v") val generationV: GenerationV
)

data class GenerationV(
    @SerializedName("black-white") val blackWhite: BlackWhite
)

data class BlackWhite(
    val animated: Animated
)

data class Animated(
    @SerializedName("front_default") val frontDefault: String,
    @SerializedName("front_shiny") val frontShiny: String,
)

data class Tipo(
    @SerializedName("type") val tipo: NombreTipo
)

data class NombreTipo(
    @SerializedName("name") val nombreTipo: String
)