package com.example.calculadorav4.Models

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calculadorav4.R
import com.example.calculadorav4.databinding.FragmentFreeSpaceBinding
import java.text.DecimalFormat


class FreeSpaceFragment : Fragment() {

    private var _binding: FragmentFreeSpaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFreeSpaceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initUI()
    }

    private fun initUI() {
        createSpinner()
    }

    private fun initListeners() {
        binding.btnFreeSpace.setOnClickListener {
            if (validateInput()) {
                val result = calculateSpaceLoss()
                navigateToResult(result)
            }
        }
    }

    private fun navigateToResult(result: Double) {
        val bundle = Bundle()
        bundle.putDouble("FREESPACE", result)
        findNavController().navigate(
            R.id.action_freeSpaceFragment_to_resultSpaceLossFragment,
            bundle
        )
    }

    private fun createSpinner() {
        //val frequencies = arrayOf("2.4GHz", "5.8GHz")
        val adapter = ArrayAdapter(
            requireContext(), R.layout.dropdown_item_project,
            resources.getStringArray(R.array.frequencies)
        )
        binding.spinnerFrequencyFreeSpace.adapter = adapter
    }

    private fun getTextFromEditText(): Double {
        return binding.distanceFreeSpace.text.toString().toDouble()
    }

    private fun getSelectedItemFromSpinner(): Double {
        val selectedItem = binding.spinnerFrequencyFreeSpace.selectedItem.toString()
        val frequencyInMHz = extractNumberFromText(selectedItem)
        // Convertir MHz a GHz
        return frequencyInMHz / 1000
    }

    private fun extractNumberFromText(text: String): Double {
        val numberPattern = Regex("\\d+\\.\\d+|\\d+")
        val match = numberPattern.find(text)
        return match?.value?.toDouble() ?: 0.0
    }

    private fun validateInput(): Boolean {
        val distanceText = binding.distanceFreeSpace.text.toString()
        //val frequencyText = binding.spinnerFrequencyFreeSpace.selectedItem.toString()

        // Comprobar si el campo de distancia está vacío
        if (distanceText.isEmpty()) {
            showToast("El campo de distancia no puede estar vacío.")
            return false
        }

        // Comprobar si el campo de distancia contiene solo números y un punto decimal opcional
        val numberPattern = Regex("^(\\d+(\\.\\d+)?)?$")
        if (!numberPattern.matches(distanceText)) {
            showToast("Por favor, ingrese solo números válidos para la distancia.")
            return false
        }
        // Comprobar si el número de distancia es positivo
        val distance = distanceText.toDoubleOrNull()
        if (distance == null || distance <= 0) {
            showToast("Por favor, ingrese una distancia positiva.")
            return false
        }

        return true
    }

    private fun calculateSpaceLoss(): Double {
        val distance = getTextFromEditText() // en kilómetros
        val frequencyGHz = getSelectedItemFromSpinner() // en GHz
        // Convertir la distancia de kilómetros a metros
        val distanceMeters = distance * 1000
        // Fórmula para calcular la pérdida en espacio libre
        val fspl =
            20 * Math.log10(distanceMeters.toDouble()) + 20 * Math.log10(frequencyGHz.toDouble()) + 32.45
        // Mostrar el resultado en la consola
        val df = DecimalFormat("#.##")
        Log.d("FreeSpaceLoss", "Pérdida en espacio libre: ${df.format(fspl)} dB")
        return df.format(fspl).toDouble()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}