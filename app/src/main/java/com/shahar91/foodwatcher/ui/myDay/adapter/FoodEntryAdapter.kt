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

class FoodEntryAdapter(private val listener: FoodEntryInteractionListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(FoodEntryDiffCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<FoodEntryAndFoodItem>?) {
        adapterScope.launch {
            list?.let {
                val eventualList: ArrayList<DataItem> = arrayListOf()

                val groupedList = list.groupBy { it.foodEntry.meal }

                //TODO: extract String values!!
                addHeaderAndItemToList(groupedList[Meal.BREAKFAST], DataItem.Header(Long.MIN_VALUE, "Breakfast"), eventualList)
                addHeaderAndItemToList(groupedList[Meal.LUNCH], DataItem.Header(Long.MIN_VALUE + 1, "Lunch"), eventualList)
                addHeaderAndItemToList(groupedList[Meal.DINNER], DataItem.Header(Long.MIN_VALUE + 2, "Dinner"), eventualList)
                addHeaderAndItemToList(groupedList[Meal.SNACK], DataItem.Header(Long.MIN_VALUE + 3, "Snack"), eventualList)

                withContext(Dispatchers.Main) {
                    submitList(eventualList)
                }
            }
            Log.d("SomeTag", "addHeaderAndSubmitList: ")
        }
    }

    private fun addHeaderAndItemToList(list: List<FoodEntryAndFoodItem>?, header: DataItem.Header, eventualList: ArrayList<DataItem>) {
        val breakfastList = list?.map { DataItem.FoodEntryItem(it) }
        eventualList.add(header)
        eventualList.addAll(breakfastList ?: emptyList<DataItem.FoodEntryItem>())
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