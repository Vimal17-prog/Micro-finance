package com.example.micro_finance.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.micro_finance.GramaKhataApplication
import com.example.micro_finance.R
import com.example.micro_finance.databinding.FragmentHomeBinding
import com.example.micro_finance.ui.adapter.CustomerAdapter
import com.example.micro_finance.ui.viewmodel.GramaKhataViewModel
import com.example.micro_finance.ui.viewmodel.GramaKhataViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GramaKhataViewModel by viewModels {
        GramaKhataViewModelFactory((requireActivity().application as GramaKhataApplication).repository)
    }

    private lateinit var adapter: CustomerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        val sharedPref = requireActivity().getSharedPreferences("GramaKhataPrefs", Context.MODE_PRIVATE)
        val shopName = sharedPref.getString(getString(R.string.shop_name_pref_key), "Grama-Khata")
        binding.tvShopName.text = shopName

        binding.fabAddCustomer.setOnClickListener {
            findNavController().navigate(R.id.actionHomeToAddEditCustomer)
        }

        binding.btnDueDashboard.setOnClickListener {
            findNavController().navigate(R.id.actionHomeToDueDashboard)
        }

        binding.btnDailyReport.setOnClickListener {
            findNavController().navigate(R.id.actionHomeToDailyReport)
        }

        binding.etSearch.addTextChangedListener { text ->
            viewModel.setSearchQuery(text.toString())
        }
    }

    private fun setupRecyclerView() {
        adapter = CustomerAdapter { customer ->
            val action = HomeFragmentDirections.actionHomeToCustomerDetails(customer.id)
            findNavController().navigate(action)
        }
        binding.rvCustomers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCustomers.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.customers.observe(viewLifecycleOwner) { customers ->
            adapter.submitList(customers)
            val totalBalance = customers.sumOf { it.netBalance }
            binding.tvTotalBalance.text = "Rs. $totalBalance"
            if (totalBalance < 0) {
                binding.tvTotalBalance.setTextColor(requireContext().getColor(android.R.color.holo_red_dark))
            } else {
                binding.tvTotalBalance.setTextColor(requireContext().getColor(android.R.color.holo_green_dark))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
