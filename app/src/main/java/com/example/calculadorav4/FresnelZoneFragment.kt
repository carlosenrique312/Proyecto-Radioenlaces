package com.example.calculadorav4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calculadorav4.databinding.FragmentFresnelZoneBinding
import java.text.DecimalFormat

class FresnelZoneFragment : Fragment() {

    private var _binding: FragmentFresnelZoneBinding? = null
    private val binding get() = _binding!!
    private var rx: Double = 0.0
    private var transmitterToObstacle: Double = 0.0
    private val constant = 17.31

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFresnelZoneBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initUI()
    }

    private fun initUI() {
        createSpinnerFresnel()
    }

    private fun initListeners() {
        binding.btnFresnelZoneCalculate.setOnClickListener {
            val input = binding.firstDistanceFresnelZone.text.toString()

            // Validación del campo de texto
            if (input.isNotEmpty() && input.all { it.isDigit() }) {
                transmitterToObstacle = input.toDouble()
                val result = calculateFresnelZone()
                navigateToResult(result)
            } else {
                // Mostrar un Toast como mensaje de error
                Toast.makeText(requireContext(), "Ingresa un número positivo válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createSpinnerFresnel() {
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.frequencies)
        )
        binding.spinnerFrecuencyFreeSpace.adapter = adapter
    }

    private fun getTextFresnel(factFresnel: EditText): Double {
        return factFresnel.text.toString().toDouble()
    }

    private fun getSelectedItemFromSpinner(): Double {
        val selectedItem = binding.spinnerFrecuencyFreeSpace.selectedItem.toString()
        val frequencyInMHz = extractNumberFromText(selectedItem)
        return frequencyInMHz / 1000 // Convierte MHz a GHz
    }

    private fun extractNumberFromText(text: String): Double {
        val numberPattern = Regex("\\d+\\.\\d+|\\d+")
        val match = numberPattern.find(text)
        return match?.value?.toDouble() ?: 0.0
    }

    private fun calculateFresnelZone(): Double {
        val frequencyFresnelGHz = getSelectedItemFromSpinner()
        rx = constant * Math.sqrt(transmitterToObstacle / (4 * frequencyFresnelGHz))
        val df = DecimalFormat("#.##")
        return df.format(rx).toDouble()
    }

    private fun navigateToResult(result: Double) {
        val bundle = Bundle()
        bundle.putDouble("FRESULT", result)
        findNavController().navigate(
            R.id.action_fresnelZoneFragment_to_fresnelZoneResultFragment,
            bundle
        )
    }

}
