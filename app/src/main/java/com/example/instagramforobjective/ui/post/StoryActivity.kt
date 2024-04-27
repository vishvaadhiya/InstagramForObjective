package com.example.instagramforobjective.ui.post

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivityStoryBinding
import com.example.instagramforobjective.ui.model.Story
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.goToMainActivity
import com.example.instagramforobjective.utility.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class StoryActivity : BaseActivity() {

    lateinit var binding: ActivityStoryBinding
    private var imageUrl: String? = null

    override fun initComponents() {
        Log.d(javaClass.simpleName, "initComponents: StoryActivity ")

        com.example.instagramforobjective.utility.ProgressDialog.showDialog(this as AppCompatActivity)
        imageUrl = intent.getStringExtra(Constants.IMAGE_URI)
        if (!imageUrl.isNullOrEmpty()) {
            com.example.instagramforobjective.utility.ProgressDialog.hideDialog()
            Glide.with(this)
                .load(imageUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        com.example.instagramforobjective.utility.ProgressDialog.hideDialog()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        com.example.instagramforobjective.utility.ProgressDialog.hideDialog()
                        return false
                    }
                }).into(binding.storyIv)
        } else {
            Toast.makeText(this, "Image URL is null or empty", Toast.LENGTH_SHORT).show()
        }

        binding.cancelStoryBtn.setOnClickListener {
            goToMainActivity()
        }
        binding.postStoryBtn.setOnClickListener {
            postStoryYourData()
        }

        binding.backPress.setOnClickListener {
            goToMainActivity()
        }
    }

    override fun defineLayout(): Int {
        return R.layout.activity_story
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityStoryBinding
    }

    private fun postStoryYourData() {
        if (imageUrl != null) {
            val storyId =  UUID.randomUUID().toString()
            val story: Story = Story(
                storyUrl = imageUrl!!,
                storyId = UUID.randomUUID().toString(),
                uid = FirebaseAuth.getInstance().currentUser!!.uid,
                time = System.currentTimeMillis().toString()
            )

            Firebase.firestore.collection(Constants.STORY).document().set(story)
                .addOnSuccessListener {
                    goToMainActivity()
                }
        } else {
            showToast(getString(R.string.please_upload_image_first))
        }
    }
}