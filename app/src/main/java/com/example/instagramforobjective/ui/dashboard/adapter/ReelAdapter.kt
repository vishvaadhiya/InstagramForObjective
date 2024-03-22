package com.example.instagramforobjective.ui.dashboard.adapter

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseAdapter
import com.example.instagramforobjective.databinding.ReelListLayoutBinding
import com.example.instagramforobjective.ui.model.Reel
import com.example.instagramforobjective.utility.ProgressDialog

class ReelAdapter(var context:Context,var reelList:ArrayList<Reel>) :BaseAdapter() {
    override fun getDataAtPosition(position: Int): Any {
        return reelList[position]
    }

    override fun getLayoutIdAtPosition(position: Int): Int {
        return R.layout.reel_list_layout
    }

    override fun itemViewDataBinding(viewDataBinding: ViewDataBinding, position: Int) {
        if (viewDataBinding is ReelListLayoutBinding){
            val videoUri = Uri.parse(reelList[position].reelUrl)
            ProgressDialog.showDialog(context as AppCompatActivity)
            viewDataBinding.videoView.setVideoURI(videoUri)
            viewDataBinding.videoView.requestFocus()
            ProgressDialog.hideDialog()
            viewDataBinding.videoView.start()
        }
    }

    override fun getItemCount(): Int {
        return reelList.size
    }
}