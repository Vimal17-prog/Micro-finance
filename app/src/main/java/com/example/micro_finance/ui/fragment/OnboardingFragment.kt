package com.example.micro_finance.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.micro_finance.R
import com.example.micro_finance.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGetStarted.setOnClickListener {
            val shopName = binding.etShopName.text.toString().trim()
            if (shopName.isNotEmpty()) {
                saveShopNameAndFinish(shopName)
            } else {
                binding.tilShopName.error = getString(R.string.error_empty_shop_name)
            }
        }
    }

    private fun saveShopNameAndFinish(shopName: String) {
        val sharedPref = requireActivity().getSharedPreferences("GramaKhataPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(getString(R.string.shop_name_pref_key), shopName)
            putBoolean(getString(R.string.onboarding_completed_pref_key), true)
            apply()
        }
        findNavController().navigate(R.id.actionOnboardingToHome)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
