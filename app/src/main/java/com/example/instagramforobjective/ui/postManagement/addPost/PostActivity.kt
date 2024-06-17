package com.example.instagramforobjective.ui.postManagement.addPost

import android.graphics.drawable.Drawable
import android.net.Uri
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
import com.example.instagramforobjective.databinding.ActivityPostBinding
import com.example.instagramforobjective.utils.uploadImage

class PostActivity : BaseActivity() {

    lateinit var binding: ActivityPostBinding
    private var imageUrl: String? = null
    private val postViewModel: PostViewModel by viewModels()

    override fun initComponents() {
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
                }).into(binding.postImage)
        } else {
            showToast("Image URL is null or empty")
        }

        binding.cancelBtn.setOnClickListener {
            goToMainActivity()
        }
        binding.postBtn.setOnClickListener {
            val image = Uri.parse(imageUrl)
            uploadImage(this,image,Constants.POST_FOLDER){ uploadedImageUrl->
                if (uploadedImageUrl!=null){
                    postYourData(uploadedImageUrl)
                }
            }
        }

        binding.backPress.setOnClickListener {
            goToMainActivity()
        }
    }

    override fun defineLayout(): Int {
        return R.layout.activity_post
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityPostBinding
    }

    private fun postYourData(image:String?) {
        ProgressDialog.showDialog(this)
        postViewModel.postYourImage(
            image, binding.captionET.editableText.toString(),
            onSuccess = {
                ProgressDialog.hideDialog()
                goToMainActivity()
            },
            onError = { errorMessage ->
                showToast(errorMessage)
            }
        )
    }
}
