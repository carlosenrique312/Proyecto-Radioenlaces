package com.example.calculadorav4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
        initListeners()
    }

    private fun initListeners() {
        //binding.btnRecalculateLink.setOnClickListener { onBackPressed() }
    }

    private fun initUI(result: Double) {
        binding.tvResultLinkBudget.text = result.toString()
        when (result) {
            in 0.00..99.99 -> {
                binding.tvRangeLink.text = getString(R.string.tittlelinkbudgetunderhundred)
                binding.tvRangeLink.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.blue
                    )
                )
                binding.tvLinkDescription.text =
                    getString(R.string.inkbudgetunderhundreddescription)
            }

            in 100.00..1000.00 -> {
                binding.tvRangeLink.text = getString(R.string.tittlelinkbudgetuptohundred)
                binding.tvRangeLink.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                binding.tvLinkDescription.text = getString(R.string.inkbudgetuptohundreddescription)
            }
        }
    }

}