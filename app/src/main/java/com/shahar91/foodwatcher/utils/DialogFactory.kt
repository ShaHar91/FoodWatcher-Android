package com.shahar91.foodwatcher.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogFactory {
    fun showConfirmationDialog(context: Context, title: String, content: String, onDeleteConfirmed: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton("Yes") { _, _ -> onDeleteConfirmed() }
            .setNegativeButton("cancel") { _, _ -> }
            .show()
    }
}