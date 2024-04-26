package com.example.instagram.ui.postManagement.addPost

import android.graphics.drawable.Drawable
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.instagram.base.BaseActivity
import com.example.instagram.ui.postManagement.shared.viewModels.PostViewModel
import com.example.instagram.utils.Constants
import com.example.instagram.utils.customViews.ProgressDialog
import com.example.instagram.utils.goToMainActivity
import com.example.instagram.utils.showToast
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.ActivityPostBinding

class PostActivity : BaseActivity() {

    lateinit var binding: ActivityPostBinding
    private var imageUrl: String? = null
    private val postViewModel: PostViewModel by viewModels()

    override fun initComponents() {
//        ProgressDialog.showDialog(this as AppCompatActivity)
        ProgressDialog.getInstance(this).show()
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
//                        ProgressDialog.hideDialog()
                        ProgressDialog.getInstance(this@PostActivity).hide()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        ProgressDialog.getInstance(this@PostActivity).hide()
//                        ProgressDialog.hideDialog()
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
            postYourData()
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

    private fun postYourData() {
        ProgressDialog.getInstance(this).show()
//        ProgressDialog.showDialog(this as AppCompatActivity)
        postViewModel.postYourImage(
            imageUrl, binding.captionET.editableText.toString(),
            onSuccess = {
                ProgressDialog.getInstance(this).hide()
//                ProgressDialog.hideDialog()
                goToMainActivity()
            },
            onError = { errorMessage ->
                showToast(errorMessage)
            }
        )
    }
}


/*if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
                val image = Uri.parse(imageUrl)
                uploadImage(this, image,Constants.POST_FOLDER){
                    if (it != null){
                        postYourData(it.toString())
                    }
                }
            }else{
            }*/