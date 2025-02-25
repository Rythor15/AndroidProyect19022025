package com.example.androidproyect19022025.data.db

import android.app.Application
import android.content.Context

//Simplemente es la clase para guardar la informacion de la base de datos

class Aplicacion: Application(){
    companion object{
        //Si se aumenta la version se borraran todos los registros

        const val VERSION=1
        const val DB="Base_1"
        const val TABLA = "amigos"
        lateinit var contexto: Context
        lateinit var llave: MyDB
    }

    override fun onCreate() {
        super.onCreate()
        contexto = applicationContext
        llave = MyDB()
    }
}