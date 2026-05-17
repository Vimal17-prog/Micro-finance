package com.example.micro_finance.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.micro_finance.data.model.Transaction

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE customerId = :customerId ORDER BY date DESC")
    fun getTransactionsForCustomer(customerId: Long): LiveData<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT SUM(amount) FROM transactions WHERE customerId = :customerId")
    suspend fun getNetBalanceForCustomer(customerId: Long): Double?

    @Query("SELECT * FROM transactions WHERE date >= :startOfDay ORDER BY date DESC")
    fun getTodayTransactions(startOfDay: Long): LiveData<List<Transaction>>
}
