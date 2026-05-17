package com.example.micro_finance

import android.app.Application
import com.example.micro_finance.data.db.AppDatabase
import com.example.micro_finance.data.repository.GramaKhataRepository

class GramaKhataApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { GramaKhataRepository(database.customerDao(), database.transactionDao()) }
}
