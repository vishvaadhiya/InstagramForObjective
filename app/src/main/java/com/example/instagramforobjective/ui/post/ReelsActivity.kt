package com.example.instagramforobjective.ui.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivityReelsBinding

class ReelsActivity : BaseActivity() {

    lateinit var binding: ActivityReelsBinding

    override fun initComponents() {
        binding.postVideo.setOnClickListener {

        }
        binding.cancelReelBtn.setOnClickListener {

        }
        binding.postReelBtn.setOnClickListener {

        }
    }

    override fun defineLayout(): Int {
        return R.layout.activity_reels
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityReelsBinding
    }

}