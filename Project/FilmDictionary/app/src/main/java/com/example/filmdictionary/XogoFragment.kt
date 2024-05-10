package com.example.filmdictionary

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.filmdictionary.databinding.FragmentXogoBinding
import com.google.android.material.chip.Chip

class XogoFragment : Fragment() {

    private var _binding: FragmentXogoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BusquedaViewModel by viewModels()
    private var puntos = 0

    private lateinit var conceptos: List<Pair<String, String>>

    private var indiceConceptoActual = 0

    private lateinit var conceptoActual: String
    var lives = 5

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentXogoBinding.inflate(inflater, container, false)
        val view = binding.root

        // Inicializa la base de datos y carga el juego
        val dbHelper = DatabaseHelper(requireContext())
        val database = dbHelper.writableDatabase
        cargarJuego(database)

        binding.btProbar.setOnClickListener {
            comprobarSeleccion()
        }

        return view
    }

    // Inicializamos o xogo creando os randoms dos conceptos
    private fun cargarJuego(database: SQLiteDatabase) {
        conceptos = viewModel.selectRandomConcepts(requireContext(), 10)
        conceptos.forEachIndexed { index, concepto ->
            val chip = binding.chipgroup.getChildAt(index) as Chip
            chip.text = concepto.first
        }

        cargarSiguienteConcepto()
    }

    // Lóxica do xogo se comprobamos a selección
    private fun comprobarSeleccion() {
        val seleccionadoId = binding.chipgroup.checkedChipId
        val chipSeleccionado = binding.root.findViewById<Chip>(seleccionadoId)

        // Primeiro vemos se nos quedan vidas
        if (lives > 0){
            // Logo verificamos se hai chip seleccionado
            if (chipSeleccionado != null) {
                val textoSeleccionado = chipSeleccionado.text.toString()
                val resultado = viewModel.verificarSeleccion(textoSeleccionado, conceptoActual)

                // Se o resultado é verdadeiro, é dicir, acertamos:
                if (resultado) {
                    puntos++
                    Toast.makeText(requireContext(), "Correcto! Gañaches un punto. Total de puntos: $puntos", Toast.LENGTH_SHORT).show()
                    binding.chipgroup.removeView(chipSeleccionado)
                    conceptos = conceptos.filter{it.first != conceptoActual}
                    if(conceptos.size > 0 && lives > 0){
                        cargarSiguienteConcepto()
                    } else {
                        binding.idDefinicion.text = "Completaches todos os conceptos!"
                            binding.btProbar.visibility = View.GONE
                            binding.btProbar.isEnabled = false
                    }
                // Se non acertamos o concepto restamos unha vida
                } else {
                    lives--
                    Toast.makeText(requireContext(), "Opción incorrecta. Inténtao de novo.\nQuédanche $lives vidas", Toast.LENGTH_LONG).show()
                }
            // Se non temos nada seleccionado
            } else {
                Toast.makeText(requireContext(), "Selecciona un concepto", Toast.LENGTH_SHORT).show()
            }
        // Se non quedan vidas
        } else {
            binding.idDefinicion.text = "Non che quedan vidas!"
            binding.btProbar.visibility = View.GONE
            binding.btProbar.isEnabled = false
        }

    }

    // Cargar seguinte concepto ao azar
    private fun cargarSiguienteConcepto() {
        if(conceptos.size > 0 || lives > 0){
            val indiceAleatorio = (0 until conceptos.size).random()
            val nuevoConcepto = conceptos[indiceAleatorio]
            binding.idDefinicion.text = nuevoConcepto.second
            conceptoActual = nuevoConcepto.first
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
