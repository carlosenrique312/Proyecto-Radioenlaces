package com.example.calculadorav4.Models

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calculadorav4.R
import com.example.calculadorav4.databinding.FragmentFresnelZoneBinding
import java.text.DecimalFormat

class FresnelZoneFragment : Fragment() {

    private var _binding: FragmentFresnelZoneBinding? = null
    private val binding get() = _binding!!
    private val constant = 548

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFresnelZoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initListeners()
    }

    private fun initUI() {
        createSpinnerFresnel()
    }

    private fun initListeners() {
        binding.btnFresnelZoneCalculate.setOnClickListener {
            if (validateInputFresnel(binding.firstDistanceFresnelZone, binding.secondDistanceFresnelZone)) {
                val result = calculateFresnelZone()
                navigateToResult(result)
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos con valores válidos (no negativos)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInputFresnel(firstDistance: EditText, secondDistance: EditText): Boolean {
        val firstValue = getTextFresnel(firstDistance)
        val secondValue = getTextFresnel(secondDistance)

        // Verificar que los valores sean mayores o iguales a 0
        return firstValue >= 0 && secondValue >= 0
    }

    private fun createSpinnerFresnel() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item_project,
            resources.getStringArray(R.array.frequencies)
        )
        binding.spinnerFrequencyFresnel.adapter = adapter
    }

    private fun getTextFresnel(editText: EditText): Double {
        val text = editText.text.toString()
        return if (text.isNotEmpty()) {
            try {
                text.toDouble() // Intentar convertir a Double
            } catch (e: NumberFormatException) {
                -1.0 // Retorna -1 si el formato es incorrecto
            }
        } else {
            -1.0 // Retorna -1 si el campo está vacío
        }
    }

    private fun getSelectedItemFromSpinner(): Double {
        val frequencyInMHz = extractNumberFromText(binding.spinnerFrequencyFresnel.selectedItem.toString())
        return frequencyInMHz
    }

    private fun extractNumberFromText(text: String): Double {
        val numberPattern = Regex("\\d+\\.\\d+|\\d+")
        return numberPattern.find(text)?.value?.toDouble() ?: 0.0
    }

    private fun calculateFresnelZone(): Double {
        val firstDistanceFresnel = getTextFresnel(binding.firstDistanceFresnelZone)
        val secondDistanceFresnel = getTextFresnel(binding.secondDistanceFresnelZone)
        val totalDistanceFresnel = firstDistanceFresnel + secondDistanceFresnel
        val frequencyFresnelGHz = getSelectedItemFromSpinner()

        // Calcular el resultado solo si los valores son válidos
        if (firstDistanceFresnel >= 0 && secondDistanceFresnel >= 0 && totalDistanceFresnel > 0) {
            val result = constant * Math.sqrt((firstDistanceFresnel * secondDistanceFresnel) / (totalDistanceFresnel * frequencyFresnelGHz))
            return DecimalFormat("#.##").format(result).toDouble()
        }

        // Retornar 0 si hay un problema con los valores
        return 0.0
    }

    private fun navigateToResult(result: Double) {
        val bundle = Bundle().apply { putDouble("FRESULT", result) }
        findNavController().navigate(R.id.action_fresnelZoneFragment_to_fresnelZoneResultFragment, bundle)
    }
}
