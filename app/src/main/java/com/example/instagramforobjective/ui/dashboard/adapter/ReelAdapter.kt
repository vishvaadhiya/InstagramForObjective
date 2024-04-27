package com.example.instagramforobjective.ui.dashboard.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseAdapter
import com.example.instagramforobjective.databinding.ReelListLayoutBinding
import com.example.instagramforobjective.ui.model.Reel
import com.example.instagramforobjective.utility.ProgressDialog


class ReelAdapter(var context: Context, var reelList: ArrayList<Reel>) : BaseAdapter() {

var mediaControls: MediaController? = null

    override fun getDataAtPosition(position: Int): Any {
        return reelList[position]
    }

    override fun getLayoutIdAtPosition(position: Int): Int {
        return R.layout.reel_list_layout
    }

    override fun itemViewDataBinding(viewDataBinding: ViewDataBinding, position: Int) {

        if (viewDataBinding is ReelListLayoutBinding) {

            if (mediaControls == null) {
                mediaControls = MediaController(context)
                mediaControls!!.setAnchorView(viewDataBinding.videoView)
            }
            ProgressDialog.showDialog(context as AppCompatActivity)
            val videoUri = Uri.parse(reelList[position].reelUrl)
            viewDataBinding.videoView.setMediaController(mediaControls)
            viewDataBinding.videoView.setVideoURI(videoUri)
            viewDataBinding.videoView.setOnPreparedListener { mediaPlayer ->
                ProgressDialog.hideDialog()
                mediaPlayer.start()
            }
            viewDataBinding.videoView.setOnErrorListener { mp, what, extra ->
                ProgressDialog.hideDialog()
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return reelList.size
    }

}
