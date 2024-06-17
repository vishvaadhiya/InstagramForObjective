package com.example.instagramforobjective.ui.postManagement.addStory

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.instagramforobjective.base.BaseActivity
import com.example.instagramforobjective.ui.postManagement.shared.viewModels.PostViewModel
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.utils.customViews.ProgressDialog
import com.example.instagramforobjective.utils.goToMainActivity
import com.example.instagramforobjective.utils.showToast
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.ActivityStoryBinding
import com.example.instagramforobjective.utils.uploadImage

class StoryActivity : BaseActivity() {

    lateinit var binding: ActivityStoryBinding
    private var imageUrl: String? = null
    private val postViewModel: PostViewModel by viewModels()

    override fun initComponents() {
        Log.d(javaClass.simpleName, "initComponents: StoryActivity ")
        ProgressDialog.showDialog(this)
        imageUrl = intent.getStringExtra(Constants.IMAGE_URI)
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        ProgressDialog.hideDialog()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        ProgressDialog.hideDialog()
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
            ProgressDialog.showDialog(this)
            val image = Uri.parse(imageUrl)
            Log.d("StoryActivity", "Image URL: $imageUrl")
            if (image != null) {
                uploadImage(this, image, Constants.STORY_FOLDER) { uploadedImageUrl ->
                    if (uploadedImageUrl != null) {
                        postStoryYourData(uploadedImageUrl)
                    } else {
                        showToast("Failed to upload image")
                    }
                }
            } else {
                showToast("Image URL is null")
            }
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

    private fun postStoryYourData(image: String?) {
        postViewModel.postYourStoryImage(
            image,
            onSuccess = {
                ProgressDialog.hideDialog()
                goToMainActivity()
            },
            onError = { exception ->
                showToast(exception)
            }
        )
    }
}