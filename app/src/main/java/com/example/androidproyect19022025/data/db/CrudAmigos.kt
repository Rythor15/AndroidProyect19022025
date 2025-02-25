package com.example.androidproyect19022025.data.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.androidproyect19022025.data.models.AmigoModel

class CrudAmigos {

    fun create(a: AmigoModel): Long{
        //Puedes abrir la db tanto en modo escritura como en modo lectura
        // depende de si quieres solo leer registros o insertar/escribir registros.

        val con = Aplicacion.llave.writableDatabase
        return try {

            //Se ignora los posible conflicos con un nombre del juego este repetido.
            con.insertWithOnConflict(
                Aplicacion.TABLA,
                null,
                a.toContentValues(),
                SQLiteDatabase.CONFLICT_IGNORE
            )
        } catch (ex: Exception){
            ex.printStackTrace()
            -1L
        } finally {
            con.close()
        }
    }

    fun read(): MutableList<AmigoModel> {
        val lista = mutableListOf<AmigoModel>()
        val con = Aplicacion.llave.readableDatabase
        try {
            val cursor = con.query(
                //Cada fila se añade como objeto a la lista mutable.
                Aplicacion.TABLA,
                arrayOf("id", "nombre", "edad", "apellido", "imagen"),
                null,
                null,
                null,
                null,
                null,
                null
            )
            while (cursor.moveToNext()) {
                //Este orden tiene que ser el mismo que el orden del arrayOf
                val contacto = AmigoModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
                lista.add(contacto)
            }
        } catch (ex: Exception){
            ex.printStackTrace()
        } finally {
            con.close()

        }
        return lista
    }

    public fun borrar(id: Int): Boolean {
        val con = Aplicacion.llave.writableDatabase
        val contactoBorrado = con.delete(Aplicacion.TABLA, "id=?", arrayOf(id.toString()))
        con.close()
        return contactoBorrado>0
    }

    public fun actualizar(a: AmigoModel): Boolean {
        val con = Aplicacion.llave.writableDatabase
        val values = a.toContentValues()
        var filasAfectadas = 0

        val q = " select id from ${Aplicacion.TABLA} where apellido = ? AND id <> ?"
        val cursor = con.rawQuery(q, arrayOf(a.apellido, a.id.toString()))
        val existeNombreJuego = cursor.moveToFirst()

        cursor.close()
        if (!existeNombreJuego) {
            filasAfectadas = con.update(Aplicacion.TABLA, values,"id = ?", arrayOf(a.id.toString()))
        }
        con.close()
        return filasAfectadas > 0
    }

    public fun borrarTodo() {
        val con= Aplicacion.llave.writableDatabase
        con.execSQL("delete from ${Aplicacion.TABLA}")
    }

    //Funcion de extension que sirve para dar funcionalidades a clases.
    private fun AmigoModel.toContentValues(): ContentValues {
        //Las claves entre comillas tienen que coincidir con los atributos de la tablas
        return ContentValues().apply {
            put("nombre", nombre)
            put("edad", edad)
            put("apellido", apellido)
            put("imagen", imagen)
        }
    }
}