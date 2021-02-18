package com.shahar91.foodwatcher.ui.myDay

import android.os.Bundle
import android.view.View
import be.appwise.core.ui.base.BaseBindingVMFragment
import com.shahar91.foodwatcher.R
import com.shahar91.foodwatcher.databinding.FragmentMyDayBinding

class MyDayFragment : BaseBindingVMFragment<MyDayViewModel, FragmentMyDayBinding>() {

    override fun getViewModel() = MyDayViewModel::class.java
    override fun getLayout() = R.layout.fragment_my_day

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }
    }
}