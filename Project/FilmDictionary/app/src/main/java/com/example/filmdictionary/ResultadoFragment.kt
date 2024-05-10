package com.example.filmdictionary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.filmdictionary.databinding.FragmentResultadoBinding

class ResultadoFragment : Fragment() {

    private var _binding: FragmentResultadoBinding? = null
    private val binding get() = _binding!!

    val viewModel: BusquedaViewModel by viewModels(ownerProducer = {this.requireActivity()})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentResultadoBinding.inflate(inflater, container, false)
        val view = binding.root


        viewModel.resultadoBusqueda.observe(viewLifecycleOwner) { (termino, definicion) ->
            // Actualizar os textviews coas búsquedas
            binding.idConcepto.text = termino
            binding.idDefinicion.text = definicion

            // Se non se obteu unha definición, o texto imprímese en vermello
            val color = if (definicion == "Non se atopou ese resultado no diccionario.") {
                ContextCompat.getColor(requireContext()!!, R.color.colorDefinicionNoEncontrada)
            } else {
                ContextCompat.getColor(requireContext()!!, R.color.colorDefinicionEncontrada)
            }

            binding.idDefinicion.setTextColor(color)

            // Log.d("RESULT", "Termino: $termino, Definicion: $definicion")
        }

        binding.btnInicioBuscador.setOnClickListener {
            view.findNavController().navigate(R.id.action_resultadoFragment_to_buscadorFragment)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}