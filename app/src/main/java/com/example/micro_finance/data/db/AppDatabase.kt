package com.example.micro_finance.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.micro_finance.data.dao.CustomerDao
import com.example.micro_finance.data.dao.TransactionDao
import com.example.micro_finance.data.model.Customer
import com.example.micro_finance.data.model.Transaction
import com.example.micro_finance.util.Converters

@Database(entities = [Customer::class, Transaction::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "grama_khata_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
