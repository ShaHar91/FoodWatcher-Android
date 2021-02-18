package com.shahar91.foodwatcher.ui.foodItemList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import be.appwise.core.ui.base.list.BaseAdapter
import be.appwise.core.ui.base.list.BaseViewHolder
import com.shahar91.foodwatcher.data.models.FoodItem
import com.shahar91.foodwatcher.databinding.ListItemFoodItemBinding

class FoodItemAdapter(private val listener: FoodItemInteractionListener) :
    BaseAdapter<FoodItem, FoodItemAdapter.FoodItemInteractionListener, BaseViewHolder<FoodItem>>() {
    interface FoodItemInteractionListener {
        fun onFoodItemClicked(foodItem: FoodItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<FoodItem> {
        return FoodItemViewHolder(ListItemFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class FoodItemViewHolder(private val binding: ListItemFoodItemBinding) : BaseViewHolder<FoodItem>(binding.root) {
        override fun bind(item: FoodItem) {
            binding.foodItem = item
            binding.root.setOnClickListener { listener.onFoodItemClicked(item) }
        }
    }
}