package com.example.micro_finance.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.micro_finance.data.model.Transaction
import com.example.micro_finance.data.model.TransactionType
import com.example.micro_finance.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

        fun bind(transaction: Transaction) {
            binding.tvDate.text = dateFormat.format(Date(transaction.date))
            binding.tvNote.text = if (transaction.note.isEmpty()) transaction.type.name else transaction.note
            
            val prefix = if (transaction.type == TransactionType.GIVE) "-" else "+"
            binding.tvAmount.text = "$prefix Rs. ${Math.abs(transaction.amount)}"
            
            val color = if (transaction.type == TransactionType.GIVE) {
                android.R.color.holo_red_dark
            } else {
                android.R.color.holo_green_dark
            }
            binding.tvAmount.setTextColor(binding.root.context.getColor(color))
        }
    }

    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
}
