package com.shahar91.foodwatcher.ui.foodItemList

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSmoothScroller
import be.appwise.core.extensions.view.onQueryChange
import be.appwise.core.extensions.view.setupRecyclerView
import be.appwise.emptyRecyclerView.RecyclerViewState
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.reddit.indicatorfastscroll.FastScrollerView
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.data.models.FoodItem
import com.shahar91.foodwatcher.databinding.FragmentFoodItemListBinding
import com.shahar91.foodwatcher.ui.base.AppBaseBindingVMFragment
import com.shahar91.foodwatcher.ui.foodItemList.adapter.DataItem
import com.shahar91.foodwatcher.ui.foodItemList.adapter.FoodItemAdapter
import com.shahar91.foodwatcher.ui.myDay.adapter.ViewTypeItemDecoration
import com.shahar91.foodwatcher.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class FoodItemListFragment : AppBaseBindingVMFragment<FragmentFoodItemListBinding>() {

    override val mViewModel: FoodItemListViewModel by viewModel()
    override fun getLayout() = R.layout.fragment_food_item_list
    override fun getToolbar() = mBinding.mergeToolbar.mtbMain

    private lateinit var foodItemAdapter: FoodItemAdapter
    private val foodItemAdapterListener = object : FoodItemAdapter.FoodItemInteractionListener {
        override fun onFoodItemClicked(foodItem: FoodItem) {
            Log.d("something", "onFoodItemClicked: ")

            FoodItemListFragmentDirections.actionFoodItemListFragmentToAddMealEntryFragment(foodItem.id, Constants.INVALID_ID)
                .run(findNavController()::navigate)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        initViews()
        initListeners()
    }

    private fun initListeners() {
        mViewModel.foodItems.observe(viewLifecycleOwner, {
            foodItemAdapter.addHeaderAndSubmitList(it)
        })
    }

    private fun initViews() {
        foodItemAdapter = FoodItemAdapter(foodItemAdapterListener)

        mBinding.run {
            rvFoodItems.run {
                setupRecyclerView(decoration = ViewTypeItemDecoration(requireContext(), ViewTypeItemDecoration.VERTICAL).apply {
                    dividerForItemTypes = listOf(FoodItemAdapter.FoodItemTypes.FOOD_ITEM.id)
                    showDividerLastItem = false
                })
                adapter = foodItemAdapter
                state = RecyclerViewState.NORMAL
                emptyStateView = tvEmptyList
            }

            svFoodItems.onQueryChange {
                mBinding.viewModel?.setQuery(it)
                true
            }

            //TODO: when keyboard is open, hide the fastScroller!!!
            fastscroller.run {
                setupWithRecyclerView(
                    rvFoodItems, { position ->
                        foodItemAdapter.currentList[position].let {
                            when (it) {
                                //TODO: this can be refined and refactored!! Maybe place it in the viewModel?
                                is DataItem.Header -> {
                                    if (it.isFavorite) {
                                        FastScrollItemIndicator.Icon(R.drawable.ic_favorite_filled)
                                    } else {
                                        FastScrollItemIndicator.Text(it.content ?: "")
                                    }
                                }
                                is DataItem.FoodItemContent -> {
                                    if (it.foodItem.isFavorite) {
                                        FastScrollItemIndicator.Icon(R.drawable.ic_favorite_filled)
                                    } else {
                                        FastScrollItemIndicator.Text(it.foodItem.name.first().toUpperCase().toString())
                                    }
                                }
                            }
                        }
                    },
                    useDefaultScroller = false
                )
                val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(context) {
                    override fun getVerticalSnapPreference(): Int = SNAP_TO_START
                }
                itemIndicatorSelectedCallbacks += object : FastScrollerView.ItemIndicatorSelectedCallback {
                    override fun onItemIndicatorSelected(
                        indicator: FastScrollItemIndicator,
                        indicatorCenterY: Int,
                        itemPosition: Int
                    ) {
                        rvFoodItems.stopScroll()
                        smoothScroller.targetPosition = itemPosition
                        rvFoodItems.layoutManager?.startSmoothScroll(smoothScroller)
                    }
                }
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
            FoodItemListFragmentDirections.actionFoodItemListFragmentToAddFoodItemFragment(Constants.INVALID_ID).run(findNavController()::navigate)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}