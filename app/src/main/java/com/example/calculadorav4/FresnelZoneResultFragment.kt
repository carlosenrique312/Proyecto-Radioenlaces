package com.example.calculadorav4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calculadorav4.databinding.FragmentFresnelZoneResultBinding

class FresnelZoneResultFragment : Fragment() {

    private var _binding: FragmentFresnelZoneResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFresnelZoneResultBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var result = arguments?.getDouble("FRESULT")
        if (result != null) {
            initUI(result)
        }
        initListeners()
    }

    private fun initListeners() {
        //binding.btnRecalculateFresnel.setOnClickListener { onBackPressed() }
    }

    private fun initUI(result: Double) {
        binding.tvFresnelResult.text = "${result} m"
        binding.tvDescriptionFresnel.text = getString(R.string.fresnel_result_description)
    }


}