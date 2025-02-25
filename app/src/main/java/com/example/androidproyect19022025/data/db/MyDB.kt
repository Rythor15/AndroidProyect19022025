package com.example.androidproyect19022025.data.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDB(): SQLiteOpenHelper(Aplicacion.contexto, Aplicacion.DB, null, Aplicacion.VERSION)  {
    private val q = "create table ${Aplicacion.TABLA}(" +
            "id integer primary key autoincrement," +
            "nombre text not null," +
            "edad integer not null," +
            "nombreJuego text not null unique," +
            "imagen text not null);"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(q)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (newVersion>oldVersion) {
            val borrarTabla = "drop table ${Aplicacion.TABLA}"
            db?.execSQL(borrarTabla)
            onCreate(db)
        }
    }
}