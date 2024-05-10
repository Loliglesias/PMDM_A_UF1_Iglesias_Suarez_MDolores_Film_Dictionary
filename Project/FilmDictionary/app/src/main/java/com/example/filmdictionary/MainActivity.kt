package com.example.filmdictionary

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.filmdictionary.databinding.FragmentUltimasBusquedasBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var busquedaViewModel: BusquedaViewModel

    private var _binding: FragmentUltimasBusquedasBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Verificamos se xa existe a BBDD
        val databaseFile = getDatabasePath(DatabaseHelper.DATABASE_NAME)

        if (!databaseFile.exists()) {
            // Se non existe, créase de novo
            databaseHelper = DatabaseHelper(this)
            val initializer = DatabaseInitializer(this)
            initializer.importDataFromTSV()
            Log.d("RESULT", "Creada BBDD")

        } else {
            // Se a BBDD xa existe, iniciamos a clase databasehelper
            databaseHelper = DatabaseHelper(this)

           //Log.d("RESULT", "Inicializado DatabaseHelper")
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.inicio_buscador -> {
                    // Imos ao fragmento de buscador
                    navController.navigate(R.id.buscadorFragment)
                    true
                }
                R.id.historial -> {
                    // Imos ao fragmento de historial
                    navController.navigate(R.id.ultimasBusquedasFragment)
                    true
                }
                R.id.xogo -> {
                    // Imos ao fragmento de xogo
                    navController.navigate(R.id.xogoFragment)
                    true
                }
                else -> false
            }
        }

        busquedaViewModel = ViewModelProvider(this).get(BusquedaViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    // Pechamos a conexión coa BBDD
    override fun onDestroy() {
        databaseHelper.close()
        super.onDestroy()
    }

    fun getBusquedaViewModel(): BusquedaViewModel {
        return busquedaViewModel
    }

}
