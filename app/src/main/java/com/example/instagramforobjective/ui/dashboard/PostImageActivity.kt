package com.example.instagramforobjective.ui.dashboard

import android.net.Uri
import android.widget.MediaController
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivityPostImageBinding
import com.example.instagramforobjective.utility.ProgressDialog

class PostImageActivity : BaseActivity() {

    lateinit var binding: ActivityPostImageBinding
    var mediaControls: MediaController?= null

    override fun initComponents() {
        val imageUrl = intent.getStringExtra("imageUrl")
        val text = intent.getStringExtra("caption")
        val videoString = intent.getStringExtra("videoUrl")
        if (mediaControls == null) {
            mediaControls = MediaController(this)
            mediaControls!!.setAnchorView(binding.videoView)
        }

        if (videoString != null) {
            val videoUri = Uri.parse(videoString)
            binding.videoView.setMediaController(mediaControls)
            ProgressDialog.showDialog(this)
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
            binding.videoView.setVideoURI(videoUri)
            ProgressDialog.hideDialog()
            binding.videoView.start()
        } else {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(binding.image)
        }

        binding.userNameTxt.text = text
    }

    override fun defineLayout(): Int {
        return R.layout.activity_post_image
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityPostImageBinding
    }

}