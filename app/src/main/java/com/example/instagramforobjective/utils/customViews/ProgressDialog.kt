package com.example.instagramforobjective.utils.customViews

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.app.AppCompatDialog
import com.example.instagramforobjective.R

class ProgressDialog {

    companion object {

        private var dialog: Dialog? = null
        private var activity: Activity? = null

        @Synchronized
        fun showDialog(activity: Activity) {
            this.activity = activity
            if (dialog?.isShowing == true || activity.isDestroyed || activity.isFinishing) return

            try {
                dialog = AppCompatDialog(activity).apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                    setCancelable(false)
                    setContentView(R.layout.custom_loading_layout)
                    show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Optionally, handle the exception, e.g., show a Toast message
            }
        }

        @Synchronized
        fun hideDialog() {
            val currentActivity = activity ?: return
            if (dialog == null || !dialog?.isShowing!! || currentActivity.isDestroyed || currentActivity.isFinishing) return

            try {
                val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                    dialog?.window?.decorView,
                    PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.0f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.0f),
                    PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f)
                ).apply {
                    interpolator = AnticipateOvershootInterpolator()
                    duration = 400
                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationEnd(animation: Animator) {
                            try {
                                dialog?.dismiss()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationCancel(animation: Animator) {}
                        override fun onAnimationRepeat(animation: Animator) {}
                    })
                }
                scaleDown.start()
            } catch (e: Exception) {
                e.printStackTrace()
                // Optionally, handle the exception, e.g., show a Toast message
            }
        }

        @Synchronized
        fun hideDialogImmediately() {
            val currentActivity = activity ?: return
            if (dialog == null || !dialog?.isShowing!! || currentActivity.isDestroyed || currentActivity.isFinishing) return

            try {
                dialog?.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
                // Optionally, handle the exception, e.g., show a Toast message
            }
        }
    }
}
