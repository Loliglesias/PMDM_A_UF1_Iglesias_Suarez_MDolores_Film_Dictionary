package com.example.filmdictionary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.filmdictionary.databinding.FragmentBuscadorBinding

class BuscadorFragment : Fragment() {

    private var _binding: FragmentBuscadorBinding? = null
    private val binding get() = _binding!!

    val viewModel: BusquedaViewModel by viewModels(ownerProducer = {this.requireActivity()})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentBuscadorBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btBuscar.setOnClickListener {
            val concepto = binding.textMessage.text.toString().trim()

            if (concepto.isNotEmpty()){
                viewModel.buscarTermino(requireContext(), concepto)
                view.findNavController().navigate(R.id.action_buscadorFragment_to_resultadoFragment)
            } else {
               Toast.makeText(context, "Introduce un concepto", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

