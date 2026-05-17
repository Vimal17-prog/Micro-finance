package com.example.micro_finance.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.micro_finance.data.model.Customer

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): LiveData<List<Customer>>

    @Query("SELECT * FROM customers WHERE name LIKE :searchQuery OR phone LIKE :searchQuery ORDER BY name ASC")
    fun searchCustomers(searchQuery: String): LiveData<List<Customer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer)

    @Update
    suspend fun updateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Long): Customer?

    @Query("UPDATE customers SET netBalance = (SELECT SUM(amount) FROM transactions WHERE customerId = :customerId) WHERE id = :customerId")
    suspend fun updateCustomerBalance(customerId: Long)
}
