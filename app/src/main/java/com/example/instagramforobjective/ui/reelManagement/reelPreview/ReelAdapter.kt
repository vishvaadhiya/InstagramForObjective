package com.example.instagramforobjective.ui.reelManagement.reelPreview

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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


            ProgressDialog.showDialog(context as AppCompatActivity)
            val videoUri = Uri.parse(reelList[position].reelUrl)
            viewDataBinding.videoView.setVideoURI(videoUri)
            viewDataBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if (p2) {
                        viewDataBinding.videoView.seekTo(p1 * 100)
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                    viewDataBinding.videoView.pause()
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                    viewDataBinding.videoView.start()
                }
            })

            viewDataBinding.videoView.setOnPreparedListener { mediaPlayer ->
                Log.d("TAG", "itemViewDataBinding:video prepare ")
                ProgressDialog.hideDialog()
                mediaPlayer.start()
                viewDataBinding.seekBar.max = mediaPlayer.duration / 100
                val handler = Handler()
                val updateSeekBar = object : Runnable {
                    override fun run() {
                        viewDataBinding.seekBar.progress = viewDataBinding.videoView.currentPosition / 100
                        handler.postDelayed(this, 1000)
                    }
                }
                handler.postDelayed(updateSeekBar, 1000)
            }

            viewDataBinding.videoView.setOnCompletionListener {
                viewDataBinding.seekBar.progress = 0
            }

            viewDataBinding.userNameTxt.text = reelList[position].name
            Glide.with(context).load(reelList[position].profileLink)
                .into(viewDataBinding.reelProfileImage)
            viewDataBinding.reelCaptionTxt.text = reelList[position].caption
            viewDataBinding.videoView.setOnErrorListener { _, _, _ ->
                ProgressDialog.hideDialog()
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
