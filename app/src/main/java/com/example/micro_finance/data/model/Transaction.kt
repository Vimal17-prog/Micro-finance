package com.example.micro_finance.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["customerId"])]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long,
    val amount: Double, // Positive for "Give" (credit), Negative for "Take" (payment)
    val note: String,
    val date: Long = System.currentTimeMillis(),
    val type: TransactionType
)

enum class TransactionType {
    GIVE, TAKE
}
