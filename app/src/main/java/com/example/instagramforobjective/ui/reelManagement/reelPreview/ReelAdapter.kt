package com.example.instagramforobjective.ui.reelManagement.reelPreview

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.instagramforobjective.base.BaseAdapter
import com.example.instagramforobjective.data.models.Reel
import com.example.instagramforobjective.utils.customViews.ProgressDialog
import com.example.instagramforobjective.utils.showToast
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.ReelListLayoutBinding


class ReelAdapter(var context: Context, private var reelList: ArrayList<Reel>) : BaseAdapter() {

    private var mediaControls: MediaController? = null

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
            ProgressDialog.getInstance(context).show()
//            ProgressDialog.showDialog(context as AppCompatActivity)
            val videoUri = Uri.parse(reelList[position].reelUrl)
            viewDataBinding.videoView.setMediaController(mediaControls)
            viewDataBinding.videoView.setVideoURI(videoUri)
            viewDataBinding.videoView.setOnPreparedListener { mediaPlayer ->
                Log.d("TAG", "itemViewDataBinding:video prepare ")
                ProgressDialog.getInstance(context).hide()
//                ProgressDialog.hideDialog()
                mediaPlayer.start()
            }
            context.showToast(reelList.get(position).uid)
            viewDataBinding.userNameTxt.text = reelList[position].name
            Glide.with(context).load(reelList[position].profileLink)
                .into(viewDataBinding.reelProfileImage)
            viewDataBinding.reelCaptionTxt.text = reelList[position].caption
            viewDataBinding.videoView.setOnErrorListener { _, _, _ ->
                ProgressDialog.getInstance(context).hide()
//                ProgressDialog.hideDialog()
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                false
            }
        }
    }


    override fun getItemCount(): Int {
        return reelList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateReel(reelList: List<Reel>) {
        this.reelList = reelList as ArrayList<Reel>
        notifyDataSetChanged()
    }

}
