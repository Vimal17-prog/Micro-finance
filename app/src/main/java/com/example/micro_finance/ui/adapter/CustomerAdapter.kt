package com.example.micro_finance.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.micro_finance.R
import com.example.micro_finance.data.model.Customer
import com.example.micro_finance.databinding.ItemCustomerBinding

class CustomerAdapter(private val onCustomerClick: (Customer) -> Unit) :
    ListAdapter<Customer, CustomerAdapter.CustomerViewHolder>(CustomerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding = ItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CustomerViewHolder(private val binding: ItemCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(customer: Customer) {
            binding.tvCustomerName.text = customer.name
            binding.tvCustomerPhone.text = customer.phone
            binding.tvNetBalance.text = "Rs. ${customer.netBalance}"

            if (customer.netBalance < 0) {
                binding.tvNetBalance.setTextColor(binding.root.context.getColor(android.R.color.holo_red_dark))
            } else {
                binding.tvNetBalance.setTextColor(binding.root.context.getColor(android.R.color.holo_green_dark))
            }

            Glide.with(binding.ivCustomerPhoto.context)
                .load(customer.photoUri)
                .placeholder(R.drawable.ic_launcher_foreground)
                .circleCrop()
                .into(binding.ivCustomerPhoto)

            binding.root.setOnClickListener { onCustomerClick(customer) }
        }
    }

    class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>() {
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem == newItem
        }
    }
}
