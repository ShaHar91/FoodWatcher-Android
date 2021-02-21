package com.shahar91.foodwatcher.ui.foodItemList

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import be.appwise.core.extensions.view.onQueryChange
import be.appwise.core.extensions.view.setupRecyclerView
import be.appwise.core.ui.custom.RecyclerViewEnum
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.data.models.FoodItem
import com.shahar91.foodwatcher.databinding.FragmentFoodItemListBinding
import com.shahar91.foodwatcher.ui.AppBaseBindingVMFragment
import com.shahar91.foodwatcher.ui.foodItemList.adapter.FoodItemAdapter

class FoodItemListFragment : AppBaseBindingVMFragment<FoodItemListViewModel, FragmentFoodItemListBinding>() {
    override fun getViewModel() = FoodItemListViewModel::class.java
    override fun getLayout() = R.layout.fragment_food_item_list
    override fun getToolbar() = mBinding.mergeToolbar.mtbMain

    private lateinit var foodItemAdapter: FoodItemAdapter
    private val foodItemAdapterListener = object : FoodItemAdapter.FoodItemInteractionListener {
        override fun onFoodItemClicked(foodItem: FoodItem) {
            Log.d("something", "onFoodItemClicked: ")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        initViews()
        initListeners()
    }

    private fun initListeners() {
        mViewModel.foodItems.observe(viewLifecycleOwner, {
            Log.d("FoodItemListFragment", "new foodItems?: ${it.size}")
            foodItemAdapter.setItems(it)
        })
    }

    private fun initViews() {
        foodItemAdapter = FoodItemAdapter(foodItemAdapterListener)

        mBinding.apply {
            rvFoodItems.apply {
                setupRecyclerView()
                adapter = foodItemAdapter
                stateView = RecyclerViewEnum.NORMAL
            }

            svFoodItems.onQueryChange {
                mBinding.viewModel?.setQuery(it)
                true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.food_item_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_foodItemListFragment_to_addFoodItemFragment) {
            // Navigating manually as the Transition Animation weren't working
            FoodItemListFragmentDirections.actionFoodItemListFragmentToAddFoodItemFragment().run(findNavController()::navigate)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}