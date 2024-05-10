package com.example.filmdictionary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.filmdictionary.databinding.FragmentUltimasBusquedasBinding

class UltimasBusquedasFragment : Fragment() {

    private var _binding: FragmentUltimasBusquedasBinding? = null
    private val binding get() = _binding!!

    val viewModel: BusquedaViewModel by viewModels(ownerProducer = { this.requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUltimasBusquedasBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.ultimasBusquedas.observe(viewLifecycleOwner) { ultimasBusquedas ->
            actualizarBotones(ultimasBusquedas)
        }

        return view
    }

    // Actualizamos os botóns coas búsquedas que temos almacenadas
    private fun actualizarBotones(ultimasBusquedas: List<Pair<String, String>>) {
        val buttons = listOf(
            binding.concepto1, binding.concepto2, binding.concepto3, binding.concepto4,
            binding.concepto5, binding.concepto6, binding.concepto7, binding.concepto8,
            binding.concepto9, binding.concepto10
        )

        for ((index, term) in ultimasBusquedas.withIndex()) {
            if (index >= buttons.size) break

            val button = buttons[index]
            button.text = term.first
            button.visibility = View.VISIBLE
            button.setOnClickListener {
                viewModel.buscarTermino(requireContext(), term.first)

                // Imos ao fragmento de resultado buscando o termo
                val action = UltimasBusquedasFragmentDirections
                    .actionUltimasBusquedasFragmentToResultadoFragment(term.first)
                findNavController().navigate(action)
            }
        }
        // Ocultar os botóns que temos creados se hai menos de 10 búsquedas feitas
        for (i in ultimasBusquedas.size until buttons.size) {
            buttons[i].visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}