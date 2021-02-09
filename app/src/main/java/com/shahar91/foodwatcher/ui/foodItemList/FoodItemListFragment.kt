package com.shahar91.foodwatcher.ui.foodItemList

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import be.appwise.core.extensions.view.setupRecyclerView
import be.appwise.core.ui.base.BaseBindingVMFragment
import be.appwise.core.ui.custom.RecyclerViewEnum
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.data.models.FoodItem
import com.shahar91.foodwatcher.databinding.FoodItemListFragmentBinding
import com.shahar91.foodwatcher.ui.foodItemList.adapter.FoodItemAdapter

class FoodItemListFragment : BaseBindingVMFragment<FoodItemListViewModel, FoodItemListFragmentBinding>() {
    override fun getViewModel() = FoodItemListViewModel::class.java
    override fun getLayout() = R.layout.food_item_list_fragment

    private lateinit var foodItemAdapter: FoodItemAdapter
    private val foodItemAdapterListener = object : FoodItemAdapter.FoodItemInteractionListener {
        override fun onFoodItemClicked(foodItem: FoodItem) {
            Log.d("something", "onFoodItemClicked: ")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        initViews()

        mViewModel.items.observe(viewLifecycleOwner, Observer {
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
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.food_item_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // If the ID of the menuItem is the same as the ID of the Fragment in the NavController, it will navigate automatically to the correct fragment
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }
}