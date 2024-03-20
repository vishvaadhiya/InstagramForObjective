package com.example.instagramforobjective.utility

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import com.example.instagramforobjective.R


class ProgressDialog {

    private val activity: Activity? = null
    private val dialog: Dialog? = null
    private val context: Context? = null

    companion object {

        private var dialog: Dialog? = null
        private lateinit var activity: AppCompatActivity

        fun showDialog(activity: AppCompatActivity) {
            Companion.activity = activity
            if (dialog?.isShowing == true) return
            if (activity.isDestroyed || activity.isFinishing) return
            dialog = AppCompatDialog(activity)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            dialog?.setCancelable(false)
            dialog?.setContentView(R.layout.custom_loading_layout)
            dialog?.show()
        }
        fun LoadingDialog(activity: Activity) {
            Companion.activity = activity as AppCompatActivity
        }


        fun hideDialog() {
            if (dialog == null || !dialog?.isShowing!!) return

            if (activity.isDestroyed || activity.isFinishing) return

            val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                dialog?.window?.decorView,
                PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.0f),
                PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.0f),
                PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f)
            )
            scaleDown.interpolator = AnticipateOvershootInterpolator()
            scaleDown.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator) {
                    if (dialog == null || !dialog?.isShowing!!) return
                    dialog?.dismiss()
                }

                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            scaleDown.duration = 400
            scaleDown.start()
        }

        fun hideDialogImmediately() {
            if (dialog == null || !dialog?.isShowing!! || activity.isDestroyed || activity.isFinishing) return
            dialog?.dismiss()
        }

    }

}