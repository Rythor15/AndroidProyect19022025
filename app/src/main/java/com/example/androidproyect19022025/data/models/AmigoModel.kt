package com.example.androidproyect19022025.data.models

import java.io.Serializable

data class AmigoModel(
    val id: Int,
    val nombre: String,
    val edad: Int,
    val apellido: String,
    val imagen: String
): Serializable
