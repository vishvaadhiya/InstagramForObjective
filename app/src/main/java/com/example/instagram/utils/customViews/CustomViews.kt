package com.example.instagram.utils.customViews

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramforobjective.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


fun fragmentBody(
    context: Context,
    /*adapter: RecyclerView.Adapter<Adapter.ViewHolder>,*/
    clickCallback: View.OnClickListener
): View {
    val layoutParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT
    ).apply {
        this.gravity = Gravity.RIGHT or Gravity.BOTTOM
    }
    return FrameLayout(context).apply {
        this.layoutParams = layoutParams
        addView(RecyclerView(context).apply {
            layoutManager = GridLayoutManager(context, 3)
            setPadding(0, 100, 0, 0)
            this.layoutParams = layoutParams
            this.adapter = adapter
        })
        addView(FloatingActionButton(context).apply {
            this.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(32, 32, 32, 32)
                this.gravity = Gravity.RIGHT or Gravity.BOTTOM
            }
            imageTintList = ColorStateList.valueOf(Color.WHITE)
            setImageResource(R.drawable.ic_photo_camera)
            setOnClickListener(clickCallback)
        })
    }
}