package com.example.micro_finance.ui.viewmodel

import androidx.lifecycle.*
import com.example.micro_finance.data.model.Customer
import com.example.micro_finance.data.model.Transaction
import com.example.micro_finance.data.repository.GramaKhataRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class GramaKhataViewModel(private val repository: GramaKhataRepository) : ViewModel() {

    val allCustomers: LiveData<List<Customer>> = repository.allCustomers

    private val _searchQuery = MutableLiveData<String>("")
    val customers: LiveData<List<Customer>> = _searchQuery.switchMap { query ->
        if (query.isNullOrEmpty()) {
            repository.allCustomers
        } else {
            repository.searchCustomers(query)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun insertCustomer(customer: Customer) = viewModelScope.launch {
        repository.insertCustomer(customer)
    }

    fun updateCustomer(customer: Customer) = viewModelScope.launch {
        repository.updateCustomer(customer)
    }

    fun deleteCustomer(customer: Customer) = viewModelScope.launch {
        repository.deleteCustomer(customer)
    }

    fun getTransactionsForCustomer(customerId: Long): LiveData<List<Transaction>> {
        return repository.getTransactionsForCustomer(customerId)
    }

    fun insertTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.insertTransaction(transaction)
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.deleteTransaction(transaction)
    }

    fun getTodayTransactions(): LiveData<List<Transaction>> {
        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return repository.getTodayTransactions(startOfDay)
    }
}

class GramaKhataViewModelFactory(private val repository: GramaKhataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GramaKhataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GramaKhataViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
