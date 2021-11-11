package com.shahar91.foodwatcher.ui.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import be.appwise.core.ui.base.BaseBindingVMFragment
import com.google.android.material.appbar.MaterialToolbar

abstract class AppBaseBindingVMFragment<B : ViewDataBinding> : BaseBindingVMFragment<B>() {

    protected abstract fun getToolbar(): MaterialToolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        getToolbar().run {
            (requireActivity() as AppCompatActivity).setSupportActionBar(this)
            setupWithNavController(navController, appBarConfiguration)
        }

        setHasOptionsMenu(true)
    }
}