package com.example.micro_finance.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.micro_finance.R
import com.example.micro_finance.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            if (_binding == null) return@postDelayed
            
            val sharedPref = requireActivity().getSharedPreferences("GramaKhataPrefs", Context.MODE_PRIVATE)
            val onboardingCompleted = sharedPref.getBoolean(getString(R.string.onboarding_completed_pref_key), false)

            if (onboardingCompleted) {
                findNavController().navigate(R.id.actionSplashToHome)
            } else {
                findNavController().navigate(R.id.actionSplashToOnboarding)
            }
        }, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
