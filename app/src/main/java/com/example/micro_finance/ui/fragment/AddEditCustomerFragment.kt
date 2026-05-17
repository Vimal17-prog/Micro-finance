package com.example.micro_finance.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.micro_finance.GramaKhataApplication
import com.example.micro_finance.R
import com.example.micro_finance.data.model.Customer
import com.example.micro_finance.databinding.FragmentAddEditCustomerBinding
import com.example.micro_finance.ui.viewmodel.GramaKhataViewModel
import com.example.micro_finance.ui.viewmodel.GramaKhataViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditCustomerFragment : Fragment() {

    private var _binding: FragmentAddEditCustomerBinding? = null
    private val binding get() = _binding!!

    private val args: AddEditCustomerFragmentArgs by navArgs()
    private val viewModel: GramaKhataViewModel by viewModels {
        GramaKhataViewModelFactory((requireActivity().application as GramaKhataApplication).repository)
    }

    private var selectedPhotoUri: Uri? = null
    private var existingCustomer: Customer? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedPhotoUri = uri
            Glide.with(this).load(uri).circleCrop().into(binding.ivCustomerPhoto)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.customerId != -1L) {
            loadExistingCustomer(args.customerId)
        }

        binding.fabPickPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnSave.setOnClickListener {
            saveCustomer()
        }
    }

    private fun loadExistingCustomer(id: Long) {
        // In a real app, this should be in the ViewModel
        CoroutineScope(Dispatchers.IO).launch {
            val db = (requireActivity().application as GramaKhataApplication).database
            val customer = db.customerDao().getCustomerById(id)
            withContext(Dispatchers.Main) {
                customer?.let {
                    existingCustomer = it
                    binding.etName.setText(it.name)
                    binding.etPhone.setText(it.phone)
                    if (it.photoUri != null) {
                        selectedPhotoUri = Uri.parse(it.photoUri)
                        Glide.with(this@AddEditCustomerFragment).load(selectedPhotoUri).circleCrop().into(binding.ivCustomerPhoto)
                    }
                    binding.btnSave.text = getString(R.string.edit_customer)
                }
            }
        }
    }

    private fun saveCustomer() {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()

        if (name.isEmpty()) {
            binding.tilName.error = "Name is required"
            return
        }

        val customer = Customer(
            id = existingCustomer?.id ?: 0,
            name = name,
            phone = phone,
            photoUri = selectedPhotoUri?.toString(),
            netBalance = existingCustomer?.netBalance ?: 0.0
        )

        if (existingCustomer == null) {
            viewModel.insertCustomer(customer)
        } else {
            viewModel.updateCustomer(customer)
        }

        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
