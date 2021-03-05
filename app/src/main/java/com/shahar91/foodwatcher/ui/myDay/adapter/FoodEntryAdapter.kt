package com.shahar91.foodwatcher.ui.myDay.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.appwise.core.ui.base.list.BaseViewHolder
import com.shahar91.foodwatcher.data.models.Meal
import com.shahar91.foodwatcher.data.relations.FoodEntryAndFoodItem
import com.shahar91.foodwatcher.databinding.ListItemEntryBinding
import com.shahar91.foodwatcher.databinding.ListItemEntryHeaderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

//TODO: add swipeToDelete
class FoodEntryAdapter(private val listener: FoodEntryInteractionListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(FoodEntryDiffCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<FoodEntryAndFoodItem>?) {
        adapterScope.launch {
            list?.let {
                val dataItemList: MutableList<DataItem> = mutableListOf()

                val groupedList = list.groupBy { it.foodEntry.meal }

                //TODO: extract String values!!
                Meal.values().forEach {
                    dataItemList.add(DataItem.Header(it.listId, it.content))
                    dataItemList.addAll(groupedList[it]?.map { item -> DataItem.FoodEntryItem(item) } ?: emptyList())
                }

                withContext(Dispatchers.Main) {
                    submitList(dataItemList)
                }
            }
            Log.d("SomeTag", "addHeaderAndSubmitList: ")
        }
    }

    interface FoodEntryInteractionListener {
        fun onFoodEntryClicked(foodEntryAndFoodItem: FoodEntryAndFoodItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> FoodEntryHeaderViewHolder(ListItemEntryHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ITEM_VIEW_TYPE_ITEM -> FoodEntryViewHolder(ListItemEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FoodEntryHeaderViewHolder -> {
                val item = getItem(position) as DataItem.Header
                holder.bind(item.content)
            }
            is FoodEntryViewHolder -> {
                val item = getItem(position) as DataItem.FoodEntryItem
                holder.bind(item.foodEntryAndFoodItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.FoodEntryItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    inner class FoodEntryHeaderViewHolder(private val binding: ListItemEntryHeaderBinding) : BaseViewHolder<String>(binding.root) {
        override fun bind(item: String) {
            binding.headerName = item
        }
    }

    inner class FoodEntryViewHolder(private val binding: ListItemEntryBinding) : BaseViewHolder<FoodEntryAndFoodItem>(binding.root) {
        override fun bind(item: FoodEntryAndFoodItem) {
            binding.item = item
            binding.root.setOnClickListener { listener.onFoodEntryClicked(item) }
        }
    }
}

class FoodEntryDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    data class FoodEntryItem(val foodEntryAndFoodItem: FoodEntryAndFoodItem) : DataItem() {
        override val id = foodEntryAndFoodItem.foodEntry.id.toLong()
    }

    data class Header(override val id: Long, val content: String) : DataItem()

    abstract val id: Long
}