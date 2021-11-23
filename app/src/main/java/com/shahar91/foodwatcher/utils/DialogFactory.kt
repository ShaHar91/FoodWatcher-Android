package com.shahar91.foodwatcher.utils

import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.shahar91.foodwatcher.R

object DialogFactory {
    fun showConfirmationDialog(context: Context, title: String, content: String, onDeleteConfirmed: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton(android.R.string.ok) { _, _ -> onDeleteConfirmed() }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .show()
    }

    fun showInputInformationDialog(context: Context, description: String, onSaveClicked: (String) -> Unit) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_custom_input, null)
        val inputView = view.findViewById<TextInputLayout>(R.id.md_input_layout).apply {
            editText?.setText(description)
        }

        MaterialAlertDialogBuilder(context)
            .setMessage(R.string.dialog_day_description_message)
            .setView(view)
            .setPositiveButton(R.string.common_save) { _, _ ->
                onSaveClicked(inputView.editText?.text.toString())
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .setCancelable(false)
            .show()
    }
}