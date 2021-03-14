package com.shahar91.foodwatcher.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogFactory {
    fun showDeleteConfirmationDialog(context: Context, itemName: String, onDeleteConfirmed: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setMessage("This will delete '$itemName' for eternity, are you sure you want to delete it?")
            .setTitle("Delete $itemName")
            .setPositiveButton("Yes") { _, _ -> onDeleteConfirmed() }
            .setNegativeButton("cancel") { _, _ -> }
            .show()
    }
}