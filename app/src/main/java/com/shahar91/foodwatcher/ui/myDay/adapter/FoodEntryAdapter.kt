package com.shahar91.foodwatcher.ui.myDay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.appwise.core.ui.base.list.BaseViewHolder
import com.shahar91.foodwatcher.data.models.FoodEntry
import com.shahar91.foodwatcher.data.models.Meal
import com.shahar91.foodwatcher.databinding.ListItemEmptyBinding
import com.shahar91.foodwatcher.databinding.ListItemEntryBinding
import com.shahar91.foodwatcher.databinding.ListItemEntryHeaderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//TODO: add swipeToDelete
class FoodEntryAdapter(private val listener: FoodEntryInteractionListener) :
    ListAdapter<FoodEntryAdapter.DataItem, RecyclerView.ViewHolder>(FOOD_ENTRY_DIFF) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    sealed class DataItem {
        data class FoodEntryItem(val foodEntry: FoodEntry) : DataItem()
        data class Header(val content: String) : DataItem()
        data class Empty(val meal: Meal) : DataItem()
    }

    enum class FoodEntryTypes(val id: Int) {
        HEADER(0),
        FOOD_ENTRY(1),
        EMPTY(2)
    }

    companion object {
        private val FOOD_ENTRY_DIFF = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                val areHeaderSame = oldItem is DataItem.Header && newItem is DataItem.Header && oldItem == newItem
                val areEmptySame = oldItem is DataItem.Empty && newItem is DataItem.Empty && oldItem == newItem
                val areFoodEntryItemsSame = oldItem is DataItem.FoodEntryItem && newItem is DataItem.FoodEntryItem &&
                        oldItem.foodEntry.id == newItem.foodEntry.id
                return areHeaderSame || areEmptySame || areFoodEntryItemsSame
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                val areHeaderSame = oldItem is DataItem.Header && newItem is DataItem.Header && oldItem == newItem
                val areEmptySame = oldItem is DataItem.Empty && newItem is DataItem.Empty && oldItem == newItem
                val areFoodEntryItemsSame = oldItem is DataItem.FoodEntryItem && newItem is DataItem.FoodEntryItem &&
                        oldItem.foodEntry == newItem.foodEntry
                return areHeaderSame || areEmptySame || areFoodEntryItemsSame
            }
        }
    }

    interface FoodEntryInteractionListener {
        fun onFoodEntryClicked(foodEntry: FoodEntry)
        fun onFoodEntryLongClicked(foodEntry: FoodEntry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FoodEntryTypes.HEADER.id ->
                FoodEntryHeaderViewHolder(ListItemEntryHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            FoodEntryTypes.FOOD_ENTRY.id ->
                FoodEntryViewHolder(ListItemEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            FoodEntryTypes.EMPTY.id ->
                EmptyViewHolder(ListItemEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else ->
                throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FoodEntryHeaderViewHolder -> holder.bind((getItem(position) as DataItem.Header).content)
            is FoodEntryViewHolder -> holder.bind((getItem(position) as DataItem.FoodEntryItem).foodEntry)
            is EmptyViewHolder -> holder.bind((getItem(position) as DataItem.Empty).meal)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> FoodEntryTypes.HEADER.id
            is DataItem.FoodEntryItem -> FoodEntryTypes.FOOD_ENTRY.id
            is DataItem.Empty -> FoodEntryTypes.EMPTY.id
        }
    }

    inner class FoodEntryHeaderViewHolder(private val binding: ListItemEntryHeaderBinding) : BaseViewHolder<String>(binding.root) {
        override fun bind(item: String) {
            binding.headerName = item
        }
    }

    inner class FoodEntryViewHolder(private val binding: ListItemEntryBinding) : BaseViewHolder<FoodEntry>(binding.root) {
        override fun bind(item: FoodEntry) {
            binding.item = item
            binding.root.setOnClickListener { listener.onFoodEntryClicked(item) }
            binding.root.setOnLongClickListener {
                listener.onFoodEntryLongClicked(item)
                true
            }
        }
    }

    inner class EmptyViewHolder(private val binding: ListItemEmptyBinding) : BaseViewHolder<Meal>(binding.root) {
        override fun bind(item: Meal) {
            binding.meal = item
        }
    }

    fun addHeaderAndSubmitList(list: List<FoodEntry>) = adapterScope.launch {
        val dataItemList: MutableList<DataItem> = mutableListOf()

        if (list.isNotEmpty()) {
            val groupedList = list.groupBy { it.meal }

            Meal.values().forEach {
                dataItemList.add(DataItem.Header(it.content))
                dataItemList.addAll(groupedList[it]?.map { item -> DataItem.FoodEntryItem(item) } ?: listOf(DataItem.Empty(it)))
            }
        }

        withContext(Dispatchers.Main) {
            submitList(dataItemList)
        }
    }
}