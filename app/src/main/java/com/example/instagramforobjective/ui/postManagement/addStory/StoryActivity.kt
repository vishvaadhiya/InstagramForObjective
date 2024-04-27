package com.example.instagramforobjective.ui.postManagement.addStory

import android.graphics.drawable.Drawable
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

class StoryActivity : BaseActivity() {

    lateinit var binding: ActivityStoryBinding
    private var imageUrl: String? = null
    private val postViewModel: PostViewModel by viewModels()

    override fun initComponents() {
        Log.d(javaClass.simpleName, "initComponents: StoryActivity ")
//        ProgressDialog.showDialog(this as AppCompatActivity)
        ProgressDialog.getInstance(this).show()
        imageUrl = intent.getStringExtra(Constants.IMAGE_URI)
        if (!imageUrl.isNullOrEmpty()) {
            ProgressDialog.getInstance(this).hide()
//            ProgressDialog.hideDialog()
            Glide.with(this)
                .load(imageUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        ProgressDialog.getInstance(this@StoryActivity).hide()
//                        ProgressDialog.hideDialog()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        ProgressDialog.getInstance(this@StoryActivity).hide()
//                        ProgressDialog.hideDialog()
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
            postViewModel.postYourStoryImage(
                imageUrl!!,
                onSuccess = {
                    goToMainActivity()
                },
                onError = { exception ->
                    showToast(exception)
                }
            )
        } else {
            showToast(getString(R.string.please_upload_image_first))
        }
    }
}