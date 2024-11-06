package com.example.calculadorav4.Models

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.calculadorav4.R
import com.example.calculadorav4.databinding.FragmentResultSpaceLossBinding

class ResultSpaceLossFragment : Fragment() {

    private var _binding: FragmentResultSpaceLossBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultSpaceLossBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var result = arguments?.getDouble("FREESPACE")
        if (result != null) {
            initUI(result)
        }
    }

    private fun initUI(result: Double) {
        binding.tvResultFreeSpace.text = result.toString()
        binding.tvTittleResultFreeSpace.text = getString(R.string.tittle_result_free_space_loss)
        binding.tvTittleResultFreeSpace.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.blue
            )
        )
        binding.tvFslDescription.text = getString(R.string.description_result_free_space_loss)
    }

}