package com.example.micro_finance.data.repository

import androidx.lifecycle.LiveData
import com.example.micro_finance.data.dao.CustomerDao
import com.example.micro_finance.data.dao.TransactionDao
import com.example.micro_finance.data.model.Customer
import com.example.micro_finance.data.model.Transaction

class GramaKhataRepository(
    private val customerDao: CustomerDao,
    private val transactionDao: TransactionDao
) {
    val allCustomers: LiveData<List<Customer>> = customerDao.getAllCustomers()

    fun searchCustomers(query: String): LiveData<List<Customer>> {
        return customerDao.searchCustomers("%$query%")
    }

    suspend fun insertCustomer(customer: Customer) {
        customerDao.insertCustomer(customer)
    }

    suspend fun updateCustomer(customer: Customer) {
        customerDao.updateCustomer(customer)
    }

    suspend fun deleteCustomer(customer: Customer) {
        customerDao.deleteCustomer(customer)
    }

    fun getTransactionsForCustomer(customerId: Long): LiveData<List<Transaction>> {
        return transactionDao.getTransactionsForCustomer(customerId)
    }

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
        // After inserting transaction, update the customer's net balance
        customerDao.updateCustomerBalance(transaction.customerId)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
        customerDao.updateCustomerBalance(transaction.customerId)
    }

    fun getTodayTransactions(startOfDay: Long): LiveData<List<Transaction>> {
        return transactionDao.getTodayTransactions(startOfDay)
    }
}
