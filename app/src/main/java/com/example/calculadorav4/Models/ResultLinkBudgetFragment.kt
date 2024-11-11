package com.example.calculadorav4.Models

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.calculadorav4.R
import com.example.calculadorav4.databinding.FragmentResultLinkBudgetBinding


class ResultLinkBudgetFragment : Fragment() {

    private var _binding: FragmentResultLinkBudgetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultLinkBudgetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var result = arguments?.getDouble("LINK")
        if (result != null) {
            initUI(result)
        }
    }

    private fun initUI(result: Double) {
        val formattedResult = "%.2f".format(result)
        binding.tvResultLinkBudget.text = "$formattedResult db"
        binding.tvRangeLink.text = getString(R.string.tittle_link_budget_result)
        binding.tvRangeLink.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.blue
            )
        )
        binding.tvLinkDescription.text =
            getString(R.string.description_link_budget_result)
    }

}