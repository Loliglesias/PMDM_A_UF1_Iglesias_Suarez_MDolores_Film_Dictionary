package com.example.filmdictionary

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BusquedaViewModel : ViewModel() {

    private val _resultadoBusqueda = MutableLiveData<Pair<String, String>>()
    val resultadoBusqueda: LiveData<Pair<String, String>> = _resultadoBusqueda

    private val _ultimasBusquedas = MutableLiveData<List<Pair<String, String>>>()
    val ultimasBusquedas: LiveData<List<Pair<String, String>>> = _ultimasBusquedas

    private val MAX_BUSQUEDAS = 10
    private val listaTerminosBuscados = mutableListOf<Pair<String, String>>()

    private val conceptos = mutableListOf<Pair<String, String>>()

    // Función para buscar un concepto na base de datos creada
    @SuppressLint("Range")
    fun buscarTermino(context: Context, texto: String) {
        val dbHelper = DatabaseHelper(context)
        val db: SQLiteDatabase = dbHelper.readableDatabase

        // Búsqueda normal na base de datos e sen distinguir maiúsculas de minúsculas
        val query = "SELECT concepto, definicion FROM terminos_cinematograficos WHERE LOWER(concepto) = LOWER(?) LIMIT 1"
        val selectionArgs = arrayOf(texto)

        val cursor: Cursor = db.rawQuery(query, selectionArgs)

        if (cursor.moveToFirst()) {
            val concepto = cursor.getString(cursor.getColumnIndex("concepto"))
            val definicion = cursor.getString(cursor.getColumnIndex("definicion"))

            Log.d("DATA", "Concepto: $concepto, Definicion: $definicion")

            _resultadoBusqueda.postValue(concepto to definicion)

            // Engadimos o termo aos 10 últimos buscados se non estaba xa
            if (!listaTerminosBuscados.contains(concepto to definicion)) {
                listaTerminosBuscados.add(concepto to definicion)
                if (listaTerminosBuscados.size > MAX_BUSQUEDAS) {
                    listaTerminosBuscados.removeAt(0) // Eliminar el término más antiguo si la lista excede el límite
                }

                _ultimasBusquedas.postValue(listaTerminosBuscados)
            }

        } else {
            val concepto = texto
            val definicion = "Non se atopou ese resultado no diccionario."

            _resultadoBusqueda.postValue(concepto to definicion)

           // Log.d("DATA", "No se encontraron resultados para el término: $texto")
        }

        cursor.close()
        db.close()
    }

    fun limpiarBusqueda() {
        _resultadoBusqueda.value = "" to ""
    }


    // Función para determinar conceptos random ao azar para o xogo
    fun selectRandomConcepts(context: Context, numberOfConcepts: Int): List<Pair<String, String>> {
        val dbHelper = DatabaseHelper(context)
        val db: SQLiteDatabase = dbHelper.readableDatabase

        val cursor = db.query(
            "terminos_cinematograficos",
            arrayOf("concepto", "definicion"),
            null,
            null,
            null,
            null,
            "RANDOM()",
            "$numberOfConcepts"
        )

        cursor.use {
            while (cursor.moveToNext()) {
                val concepto = cursor.getString(cursor.getColumnIndexOrThrow("concepto"))
                val definicion = cursor.getString(cursor.getColumnIndexOrThrow("definicion"))
                conceptos.add(concepto to definicion)
            }
        }

        db.close()
        return conceptos
    }

    fun verificarSeleccion(conceptoSeleccionado: String, conceptoActual: String): Boolean {
        return conceptoSeleccionado == conceptoActual
    }
}