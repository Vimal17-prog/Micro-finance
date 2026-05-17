package com.example.micro_finance.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.micro_finance.GramaKhataApplication
import com.example.micro_finance.R
import com.example.micro_finance.data.model.Customer
import com.example.micro_finance.data.model.Transaction
import com.example.micro_finance.data.model.TransactionType
import com.example.micro_finance.databinding.DialogAddTransactionBinding
import com.example.micro_finance.databinding.FragmentCustomerDetailsBinding
import com.example.micro_finance.ui.adapter.TransactionAdapter
import com.example.micro_finance.ui.viewmodel.GramaKhataViewModel
import com.example.micro_finance.ui.viewmodel.GramaKhataViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog

class CustomerDetailsFragment : Fragment() {

    private var _binding: FragmentCustomerDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: CustomerDetailsFragmentArgs by navArgs()
    private val viewModel: GramaKhataViewModel by viewModels {
        GramaKhataViewModelFactory((requireActivity().application as GramaKhataApplication).repository)
    }

    private lateinit var adapter: TransactionAdapter
    private var currentCustomer: Customer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomerDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        
        binding.btnEdit.setOnClickListener {
            val action = CustomerDetailsFragmentDirections.actionCustomerDetailsToAddEditCustomer(args.customerId)
            findNavController().navigate(action)
        }

        binding.btnGive.setOnClickListener { showTransactionDialog(TransactionType.GIVE) }
        binding.btnTake.setOnClickListener { showTransactionDialog(TransactionType.TAKE) }
        
        binding.btnRemind.setOnClickListener { sendReminder() }
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter()
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactions.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.allCustomers.observe(viewLifecycleOwner) { customers ->
            currentCustomer = customers.find { it.id == args.customerId }
            currentCustomer?.let {
                binding.tvCustomerName.text = it.name
                binding.tvBalance.text = "Rs. ${it.netBalance}"
                if (it.netBalance < 0) {
                    binding.tvBalance.setTextColor(requireContext().getColor(android.R.color.holo_red_dark))
                } else {
                    binding.tvBalance.setTextColor(requireContext().getColor(android.R.color.holo_green_dark))
                }
            }
        }

        viewModel.getTransactionsForCustomer(args.customerId).observe(viewLifecycleOwner) { transactions ->
            adapter.submitList(transactions)
        }
    }

    private fun showTransactionDialog(type: TransactionType) {
        val dialog = BottomSheetDialog(requireContext())
        val dialogBinding = DialogAddTransactionBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.tvTitle.text = if (type == TransactionType.GIVE) getString(R.string.give_credit) else getString(R.string.take_payment)
        dialogBinding.btnConfirm.text = if (type == TransactionType.GIVE) getString(R.string.give_credit) else getString(R.string.take_payment)

        dialogBinding.btnConfirm.setOnClickListener {
            val amountStr = dialogBinding.etAmount.text.toString()
            if (amountStr.isNotEmpty()) {
                var amount = amountStr.toDouble()
                if (type == TransactionType.GIVE) {
                    amount = -amount
                }
                
                val transaction = Transaction(
                    customerId = args.customerId,
                    amount = amount,
                    note = dialogBinding.etNote.text.toString(),
                    type = type
                )
                viewModel.insertTransaction(transaction)
                dialog.dismiss()
            } else {
                dialogBinding.tilAmount.error = "Enter amount"
            }
        }

        dialog.show()
    }

    private fun sendReminder() {
        currentCustomer?.let { customer ->
            if (customer.netBalance >= 0) {
                Toast.makeText(requireContext(), "No pending dues", Toast.LENGTH_SHORT).show()
                return
            }

            val sharedPref = requireActivity().getSharedPreferences("GramaKhataPrefs", Context.MODE_PRIVATE)
            val shopName = sharedPref.getString(getString(R.string.shop_name_pref_key), "Grama-Khata")
            val dueAmount = Math.abs(customer.netBalance).toString()
            val message = getString(R.string.reminder_message, shopName, dueAmount)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
                setPackage("com.whatsapp")
            }

            try {
                startActivity(intent)
            } catch (e: Exception) {
                val smsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:${customer.phone}")).apply {
                    putExtra("sms_body", message)
                }
                startActivity(smsIntent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
