package com.example.calculadorav4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calculadorav4.databinding.FragmentLinkBudgetBinding


class LinkBudgetFragment : Fragment() {

    private var otherLossPerMeter: Double = 0.0
    private var lossPerMeter: Double = 0.0
    private var TxOutputPower: Double = 0.0
    private var TxAntennaGain: Double = 0.0
    private var SpaceLoss: Double = 0.0
    private var RxAntennaGain: Double = 0.0
    private var RxReceiver: Double = 0.0
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

        binding.tvNavFreeSpace.setOnClickListener{
            findNavController().navigate(
                R.id.action_linkBudgetFragment_to_freeSpaceFragment
            )
        }

        binding.btnWireTransmission.setOnClickListener {
            showDialog(1)
        }

        binding.btnWireReception.setOnClickListener {
            showDialog(2)
        }

        binding.antennaRD.setOnCheckedChangeListener { _, isChecked ->
            updateEditText(binding.antennaRD.isChecked, binding.antennaRD, false)
        }

        binding.antennaLTB.setOnCheckedChangeListener { _, isChecked ->
            updateEditText(binding.antennaLTB.isChecked, binding.antennaLTB, false)
        }

        binding.antennaRP.setOnCheckedChangeListener { _, isChecked ->
            updateEditText(binding.antennaRP.isChecked, binding.antennaRP, true)
        }

        binding.antennaPWB.setOnCheckedChangeListener { _, isChecked ->
            updateEditText(binding.antennaPWB.isChecked, binding.antennaPWB, true)
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

    private fun openWebPageBasedOnCheckbox() {
        when {
            binding.antennaRD.isChecked -> openWebPage("https://dl.ubnt.com/datasheets/rocketdish/rd_ds_web.pdf")
            binding.antennaLTB.isChecked -> openWebPage("https://dl.ubnt.com/datasheets/LiteBeam/LiteBeam_AC_Gen2_DS.pdf")
            binding.antennaRP.isChecked -> openWebPage("https://dl.ubnt.com/datasheets/RocketAC/Rocket_Prism_AC_Gen2_DS.pdf")
            binding.antennaPWB.isChecked -> openWebPage("https://dl.ubnt.com/datasheets/PowerBeam_ac/PowerBeam5ac_DS.pdf")
            else -> {
                // Mensaje si ninguna checkbox está seleccionada
                Toast.makeText(
                    requireContext(),
                    "Por favor, selecciona una checkbox",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun initUI() {
    }

    private fun showDialog(buttonNumber: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_link_budget, null)
        dialogBuilder.setView(dialogView)

        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.rgCategories)
        val btnAddTask = dialogView.findViewById<Button>(R.id.btnAddTask)

        val dialog = dialogBuilder.create()

        btnAddTask.setOnClickListener {
            // Obtiene el ID del RadioButton seleccionado
            val selectedId = radioGroup.checkedRadioButtonId

            // Asigna el valor correspondiente a la variable según el botón que llamó al diálogo
            val selectedValue = when (selectedId) {
                R.id.rbCAT5 -> 0.08
                R.id.rbCAT5e -> 0.07
                R.id.rbCAT6 -> 0.06
                R.id.rbCAT6a -> 0.05
                else -> 0.0
            }

            // Almacena el valor en la variable correcta
            if (buttonNumber == 1) {
                lossPerMeter = selectedValue
            } else if (buttonNumber == 2) {
                otherLossPerMeter = selectedValue
            }

            dialog.dismiss()

            // Aquí puedes usar el valor seleccionado, por ejemplo:
            Toast.makeText(
                requireContext(),
                "Valor seleccionado: $selectedValue",
                Toast.LENGTH_SHORT
            ).show()
        }

        dialog.show()
    }

    private fun updateEditText(isChecked: Boolean, checkBox: CheckBox, isReception: Boolean) {
        if (isChecked) {
            // Desmarcar el otro checkbox
            if (isReception) {
                if (checkBox == binding.antennaRP) {
                    binding.antennaPWB.isChecked = false
                    binding.etAntennaGainReception.setText("34")
                    binding.etReceiverSensitivity.setText("-96")
                } else {
                    binding.antennaRP.isChecked = false
                    binding.etAntennaGainReception.setText("22")
                    binding.etReceiverSensitivity.setText("-96")
                }
            } else {
                if (checkBox == binding.antennaRD) {
                    binding.antennaLTB.isChecked = false
                    binding.etOutputPower.setText("28")
                    binding.etAntennaGain.setText("34")
                } else {
                    binding.antennaRD.isChecked = false
                    binding.etOutputPower.setText("25")
                    binding.etAntennaGain.setText("26")
                }
            }
        } else {
            // Limpiar EditText si no está seleccionado
            if (isReception) {
                binding.etAntennaGainReception.setText("")
                binding.etReceiverSensitivity.setText("")
            } else {
                binding.etOutputPower.setText("")
                binding.etAntennaGain.setText("")
            }
        }
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
            //Toast.makeText(fact.context, "El campo no puede estar vacío", Toast.LENGTH_SHORT).show()
            null // Retorna null si el campo está vacío
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

        // Verifica que todos los valores sean válidos
        if (TxOutputPower != null && TxAntennaGain != null && RxAntennaGain != null &&
            RxReceiver != null && SpaceLoss != null) {

            PireA = TxOutputPower - lossPerMeter + TxAntennaGain - SpaceLoss +
                    RxAntennaGain - otherLossPerMeter
            PireA -= RxReceiver
            return PireA
        } else {
            //Toast.makeText(binding.root.context, "Datos no válidos", Toast.LENGTH_SHORT).show()
            return null // O puedes manejar el caso de otra manera
        }
    }
}