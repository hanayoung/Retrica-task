package com.example.filtertoggle.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filtertoggle.databinding.ItemFilterBinding
import com.example.filtertoggle.model.FilterInfo

class FilterRVAdapter(): ListAdapter<FilterInfo, FilterRVAdapter.CustomViewHolder>(CustomComparator) {
    lateinit var itemClickListener: ItemClickListener

    interface ItemClickListener {
        fun onClick(view: View, data: FilterInfo, position: Int)
    }

    companion object CustomComparator : DiffUtil.ItemCallback<FilterInfo>() {
        override fun areItemsTheSame(oldItem: FilterInfo, newItem: FilterInfo): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: FilterInfo, newItem: FilterInfo): Boolean {
            return oldItem == newItem
        }
    }

    inner class CustomViewHolder(private val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FilterInfo) {
            binding.btn.apply {
                text = item.name
                setCompoundDrawablesWithIntrinsicBounds(item.icon, 0, 0, 0)
            }
            binding.btn.setOnClickListener {
                itemClickListener.onClick(it, item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding =
            ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}