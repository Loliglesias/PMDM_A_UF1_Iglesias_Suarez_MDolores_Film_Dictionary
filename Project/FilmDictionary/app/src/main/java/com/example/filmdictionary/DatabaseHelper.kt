package com.example.filmdictionary

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "cinema_glossary.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear a táboa na base de datos
        val createTableQuery = "CREATE TABLE terminos_cinematograficos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "concepto TEXT," +
                "definicion TEXT)"
        db.execSQL(createTableQuery)

        // Contador para o número de entradas creadas
        var numEntradasCreadas = 0

        // Insertar algunhas entradas de exemplo
        val ejemplosEntradas = listOf(
            Pair("Termino1", "Definicion1"),
            Pair("Termino2", "Definicion2"),
            Pair("Termino3", "Definicion3")
        )

        for ((concepto, definicion) in ejemplosEntradas) {
            val contentValues = ContentValues().apply {
                put("concepto", concepto)
                put("definicion", definicion)
            }
            val resultado = db.insert("terminos_cinematograficos", null, contentValues)
            if (resultado != -1L) {
                numEntradasCreadas++
            }
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Executamos o método se cambia a BBDD
        db.execSQL("DROP TABLE IF EXISTS terminos_cinematograficos")
        onCreate(db)

    }

    fun getDatabase(): SQLiteDatabase {
        return this.writableDatabase
    }
}