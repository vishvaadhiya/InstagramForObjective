package com.example.instagramforobjective.ui.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivityPostBinding

class PostActivity : BaseActivity() {

    lateinit var binding: ActivityPostBinding

    override fun initComponents() {
       binding.postImage.setOnClickListener {

       }
       binding.cancelButton.setOnClickListener {

       }
       binding.postButton.setOnClickListener {

       }
    }

    override fun defineLayout(): Int {
        return R.layout.activity_post
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityPostBinding
    }
}