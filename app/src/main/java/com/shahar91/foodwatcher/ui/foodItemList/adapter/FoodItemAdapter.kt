package com.shahar91.foodwatcher.ui.foodItemList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.appwise.core.ui.base.list.BaseViewHolder
import com.shahar91.foodwatcher.data.models.FoodItem
import com.shahar91.foodwatcher.databinding.ListItemFoodItemBinding
import com.shahar91.foodwatcher.databinding.ListItemFoodItemHeaderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodItemAdapter(private val listener: FoodItemInteractionListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(FoodItemDiffCallback()) {
    companion object {
        const val ITEM_VIEW_TYPE_HEADER = 0
        const val ITEM_VIEW_TYPE_ITEM = 1
    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<FoodItem>) {
        adapterScope.launch {
            val dataItemList: MutableList<DataItem> = mutableListOf()

            val mappedList = list.map { DataItem.FoodItemContent(it) }
            val favoriteFoodItems = mappedList.filter { it.foodItem.isFavorite }

            if (favoriteFoodItems.isNotEmpty()) {
                dataItemList.add(DataItem.Header(Long.MIN_VALUE, null, true))
                dataItemList.addAll(favoriteFoodItems)
            }

            val unFavoriteFoodItems = mappedList - favoriteFoodItems
            val groupedList = unFavoriteFoodItems.groupBy { it.foodItem.name.first().toUpperCase() }

            groupedList.onEachIndexed { index, entry ->
                dataItemList.add(DataItem.Header(Long.MIN_VALUE + 1 + index, entry.key.toString()))
                dataItemList.addAll(entry.value)
            }

            withContext(Dispatchers.Main) {
                submitList(dataItemList)
            }
        }
    }

    interface FoodItemInteractionListener {
        fun onFoodItemClicked(foodItem: FoodItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> FoodItemHeaderViewHolder(
                ListItemFoodItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            ITEM_VIEW_TYPE_ITEM -> FoodItemViewHolder(ListItemFoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FoodItemHeaderViewHolder -> {
                val item = getItem(position) as DataItem.Header
                holder.bind(item.content)
            }
            is FoodItemViewHolder -> {
                val item = getItem(position) as DataItem.FoodItemContent
                holder.bind(item.foodItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.FoodItemContent -> ITEM_VIEW_TYPE_ITEM
        }
    }

    inner class FoodItemViewHolder(private val binding: ListItemFoodItemBinding) : BaseViewHolder<FoodItem>(binding.root) {
        override fun bind(item: FoodItem) {
            binding.foodItem = item
            binding.root.setOnClickListener { listener.onFoodItemClicked(item) }
        }
    }

    inner class FoodItemHeaderViewHolder(private val binding: ListItemFoodItemHeaderBinding) : BaseViewHolder<String?>(binding.root) {
        override fun bind(item: String?) {
            binding.headerName = item
        }
    }
}

class FoodItemDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    data class FoodItemContent(val foodItem: FoodItem) : DataItem() {
        override val id = foodItem.id.toLong()
    }

    data class Header(override val id: Long, val content: String?, val isFavorite: Boolean = false) : DataItem()

    abstract val id: Long
}