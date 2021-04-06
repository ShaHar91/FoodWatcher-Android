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
            .setPositiveButton("Yes") { _, _ -> onDeleteConfirmed() }
            .setNegativeButton("cancel") { _, _ -> }
            .show()
    }

    fun showInputInformationDialog(context: Context, description: String, onSaveClicked: (String) -> Unit) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_custom_input, null)
        val inputView = view.findViewById<TextInputLayout>(R.id.md_input_layout).apply {
            editText?.setText(description)
        }

        MaterialAlertDialogBuilder(context)
            .setMessage("Provide more information about your day.")
            .setView(view)
            .setPositiveButton("save") { _, _ ->
                onSaveClicked(inputView.editText?.text.toString())
            }
            .setNegativeButton("cancel") { _, _ -> }
            .setCancelable(false)
            .show()
    }
}