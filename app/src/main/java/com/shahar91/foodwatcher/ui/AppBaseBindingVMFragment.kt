package com.shahar91.foodwatcher.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import be.appwise.core.ui.base.BaseBindingVMFragment
import be.appwise.core.ui.base.BaseViewModel
import com.google.android.material.appbar.MaterialToolbar

abstract class AppBaseBindingVMFragment<VM : BaseViewModel, B : ViewDataBinding> : BaseBindingVMFragment<VM, B>() {
    protected abstract fun getToolbar(): MaterialToolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(getToolbar())
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        getToolbar().setupWithNavController(navController, appBarConfiguration)

        setHasOptionsMenu(true)
    }
}