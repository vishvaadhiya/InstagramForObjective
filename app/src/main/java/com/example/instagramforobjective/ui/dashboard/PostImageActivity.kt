package com.example.instagramforobjective.ui.dashboard

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivityPostImageBinding

class PostImageActivity : BaseActivity() {

    lateinit var binding: ActivityPostImageBinding


    override fun initComponents() {
        Log.d(javaClass.simpleName, "initComponents: PostImageActivity ")
        val imageUrl = intent.getStringExtra("imageUrl")
        val text = intent.getStringExtra("caption")
        val videoString = intent.getStringExtra("videoUrl")

        if (videoString != null) {
            val videoUri = Uri.parse(videoString)
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
            binding.videoView.setVideoURI(videoUri)
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