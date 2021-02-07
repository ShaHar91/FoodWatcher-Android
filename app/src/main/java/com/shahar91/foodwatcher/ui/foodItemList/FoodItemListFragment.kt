package com.shahar91.foodwatcher.ui.foodItemList

import android.os.Bundle
import android.view.*
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import be.appwise.core.ui.base.BaseVMFragment
import com.shahar91.foodwatcher.R

class FoodItemListFragment : BaseVMFragment<FoodItemListViewModel>() {

    override fun getViewModel() = FoodItemListViewModel::class.java

    private lateinit var viewModel: FoodItemListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.food_item_list_fragment, container, false)
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