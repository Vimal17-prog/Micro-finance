package com.example.micro_finance.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.micro_finance.GramaKhataApplication
import com.example.micro_finance.databinding.FragmentDueDashboardBinding
import com.example.micro_finance.ui.adapter.CustomerAdapter
import com.example.micro_finance.ui.viewmodel.GramaKhataViewModel
import com.example.micro_finance.ui.viewmodel.GramaKhataViewModelFactory

class DueDashboardFragment : Fragment() {

    private var _binding: FragmentDueDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GramaKhataViewModel by viewModels {
        GramaKhataViewModelFactory((requireActivity().application as GramaKhataApplication).repository)
    }

    private lateinit var adapter: CustomerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDueDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = CustomerAdapter { customer ->
            val action = DueDashboardFragmentDirections.actionDueDashboardToCustomerDetails(customer.id)
            findNavController().navigate(action)
        }
        binding.rvDues.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDues.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.allCustomers.observe(viewLifecycleOwner) { customers ->
            val dueCustomers = customers.filter { it.netBalance < 0 }
                .sortedBy { it.netBalance }
            adapter.submitList(dueCustomers)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
