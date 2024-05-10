package com.example.filmdictionary

import android.content.ContentValues
import android.content.Context
import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.InputStreamReader

class DatabaseInitializer(private val context: Context) {

    fun importDataFromTSV() {
        val assetManager: AssetManager = context.assets

        try {
            // Facemos a entrada dende o arquivo .tsv
            val inputStream = assetManager.open("FILM DEFINITIONS.tsv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            // Obtener unha instancia de SQLiteDatabase
            val dbHelper = DatabaseHelper(context)
            val db = dbHelper.writableDatabase

            // Lemos cada liña do .tsv e engadímola
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val parts = line!!.split("\t")
                val concepto = parts[0]
                val definicion = parts[1]

                // Insertar os datos na BBDD
                val values = ContentValues().apply {
                    put("concepto", concepto)
                    put("definicion", definicion)
                }
                db.insert("terminos_cinematograficos", null, values)
            }

            // Pechamos a conexión
            db.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}