package com.example.instagramforobjective.utils.customViews

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.instagramforobjective.R


class ProgressDialog private constructor(context: Context) {
    private val customDialog: Dialog = Dialog(context)

    companion object {
        private var INSTANCE: ProgressDialog? = null

        fun getInstance(context: Context): ProgressDialog {
            if (INSTANCE == null) {
                INSTANCE = ProgressDialog(context)
            }
            return INSTANCE!!
        }
    }

    init {
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        customDialog.setCancelable(false)
        customDialog.setContentView(R.layout.custom_loading_layout)
    }

    fun show() {
        customDialog.show()
    }

    fun hide() {
        customDialog.dismiss()
    }
}