package com.example.calculadorav4.Models

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calculadorav4.R
import com.example.calculadorav4.databinding.FragmentLinkBudgetBinding


class LinkBudgetFragment : Fragment() {

    private var otherLossPerMeter: Double = 0.0
    private var lossPerMeter: Double = 0.0
    private var PireA: Double = 0.0

    private var _binding: FragmentLinkBudgetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLinkBudgetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        initListeners()
        initUI()
    }

    private fun initComponents() {
    }

    private fun initListeners() {

        binding.tvNavFreeSpace.setOnClickListener {
            findNavController().navigate(
                R.id.action_linkBudgetFragment_to_freeSpaceFragment
            )
        }

        binding.rdTransmissionAntennas.setOnCheckedChangeListener { _, isChecked ->
            val (outputPower, antennaGain) = when (isChecked) {
                R.id.rbTransmissionRDRP -> "28" to "34"
                R.id.rbTransmissionLB -> "25" to "23"
                R.id.rbTransmissionPB -> "25" to "22"
                R.id.rbTransmissionNone -> "" to ""
                else -> return@setOnCheckedChangeListener
            }
            binding.etOutputPower.setText(outputPower)
            binding.etAntennaGain.setText(antennaGain)
        }

        binding.rdReceptionAntennas.setOnCheckedChangeListener { _, checkedId ->
            val (antennaGain, receiverSensitivity) = when (checkedId) {
                R.id.rbReceptionRDRP -> "34" to "-96"
                R.id.rbReceptionLB -> "23" to "-96"
                R.id.rbReceptionPB -> "22" to "-96"
                R.id.rbReceptionNone -> "" to ""
                else -> return@setOnCheckedChangeListener
            }
            binding.etAntennaGainReception.setText(antennaGain)
            binding.etReceiverSensitivity.setText(receiverSensitivity)
        }

        binding.btnWireTransmission.setOnClickListener {
            showDialog(1)
        }

        binding.btnWireReception.setOnClickListener {
            showDialog(2)
        }
        binding.btnDatasheet.setOnClickListener {
            openWebPageBasedOnCheckbox()
        }

        binding.btnDatasheetTwo.setOnClickListener {
            openWebPageBasedOnCheckbox()
        }

        binding.btnCalculateLinkBudget.setOnClickListener {
            val result = CalculateLinkBudget()

            if (result != null) {
                navigateToResult(result)
            } else {
                // Opcional: Maneja el caso en que no hay un resultado válido
                Toast.makeText(binding.root.context, "Datos no válidos", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun initUI() {
    }

    private fun openWebPageBasedOnCheckbox() {
        when {
            binding.rbTransmissionRDRP.isChecked -> openWebPage("https://dl.ubnt.com/datasheets/rocketdish/rd_ds_web.pdf")
            binding.rbTransmissionLB.isChecked -> openWebPage("https://dl.ubnt.com/datasheets/LiteBeam/LiteBeam_AC_Gen2_DS.pdf")
            binding.rbTransmissionPB.isChecked -> openWebPage("https://dl.ubnt.com/datasheets/PowerBeam_ac/PowerBeam5ac_DS.pdf")
            binding.rbTransmissionNone.isChecked -> Toast.makeText(
                requireContext(),
                "Por favor, selecciona una checkbox",
                Toast.LENGTH_SHORT
            ).show()

            binding.rbReceptionRDRP.isChecked -> openWebPage("https://dl.ubnt.com/datasheets/rocketdish/rd_ds_web.pdf")
            binding.rbReceptionLB.isChecked -> openWebPage("https://dl.ubnt.com/datasheets/LiteBeam/LiteBeam_AC_Gen2_DS.pdf")
            binding.rbReceptionPB.isChecked -> openWebPage("https://dl.ubnt.com/datasheets/PowerBeam_ac/PowerBeam5ac_DS.pdf")
            binding.rbReceptionNone.isChecked -> Toast.makeText(
                requireContext(),
                "Por favor, selecciona una checkbox",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun showDialog(buttonNumber: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_link_budget, null)
        dialogBuilder.setView(dialogView)

        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.rgCategories)
        val btnAddTask = dialogView.findViewById<Button>(R.id.btnAddTask)
        val etMeters = dialogView.findViewById<EditText>(R.id.etMeters)

        val dialog = dialogBuilder.create()

        btnAddTask.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            val metersUser = etMeters.text.toString().trim()

            // Validar que el campo no esté vacío y contenga solo números (enteros o decimales)
            if (metersUser.isEmpty() || !isValidNumber(metersUser)) {
                Toast.makeText(requireContext(), "Por favor, ingrese un número válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedValue = when (selectedId) {
                R.id.rbCAT5 -> 0.08
                R.id.rbCAT5e -> 0.07
                R.id.rbCAT6 -> 0.06
                R.id.rbCAT6a -> 0.05
                else -> 0.0
            }

            val meters = metersUser.toDouble()
            if (buttonNumber == 1) {
                lossPerMeter = meters * selectedValue
            } else if (buttonNumber == 2) {
                otherLossPerMeter = meters * selectedValue
            }

            dialog.dismiss() // Solo se cierra si la validación es exitosa
            Log.d("DialogValue", "Loss per meter: $lossPerMeter")
            Log.d("DialogValue", "Loss per meter: $otherLossPerMeter")
            Toast.makeText(requireContext(), "Valor seleccionado: $lossPerMeter", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }

    // Función para validar si el texto es un número válido (entero o decimal)
    private fun isValidNumber(text: String): Boolean {
        return text.matches("^[0-9]*\\.?[0-9]+$".toRegex()) // Acepta números enteros y decimales
    }


    private fun navigateToResult(result: Double) {
        val bundle = Bundle()
        bundle.putDouble("LINK", result)
        findNavController().navigate(
            R.id.action_linkBudgetFragment_to_resultLinkBudgetFragment,
            bundle
        )
    }

    private fun getDoubleFromTextInputEditText(fact: EditText): Double? {
        val inputText = fact.text.toString()
        return if (inputText.isEmpty()) {
            null
        } else {
            inputText.toDouble()
        }
    }

    private fun CalculateLinkBudget(): Double? {
        val TxOutputPower = getDoubleFromTextInputEditText(binding.etOutputPower)
        val TxAntennaGain = getDoubleFromTextInputEditText(binding.etAntennaGain)
        val RxAntennaGain = getDoubleFromTextInputEditText(binding.etAntennaGainReception)
        val RxReceiver = getDoubleFromTextInputEditText(binding.etReceiverSensitivity)
        val SpaceLoss = getDoubleFromTextInputEditText(binding.etFreeSpaceLoss)

        if (TxOutputPower != null && TxAntennaGain != null && RxAntennaGain != null &&
            RxReceiver != null && SpaceLoss != null
        ) {

            PireA = TxOutputPower - lossPerMeter + TxAntennaGain - SpaceLoss +
                    RxAntennaGain - otherLossPerMeter
            PireA -= RxReceiver
            return PireA
        } else {
            return null // O puedes manejar el caso de otra manera
        }
    }
}