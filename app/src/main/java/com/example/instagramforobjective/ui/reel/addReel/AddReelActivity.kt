package com.example.instagramforobjective.ui.reel.addReel

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.instagramforobjective.base.BaseActivity
import com.example.instagramforobjective.ui.post.shared.viewModels.PostViewModel
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.utils.customViews.ProgressDialog
import com.example.instagramforobjective.utils.goToMainActivity
import com.example.instagramforobjective.utils.showToast
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.ActivityAddReelBinding
import com.example.instagramforobjective.utils.uploadReels

class AddReelActivity : BaseActivity() {

    lateinit var binding: ActivityAddReelBinding
    private var videoUrl: String? = null
    private val postViewModel: PostViewModel by viewModels()

    override fun initComponents() {
        ProgressDialog.showDialog(this)
        videoUrl = intent.getStringExtra(Constants.VIDEO_URI)
        Glide.with(this)
            .load(videoUrl)
            .apply(RequestOptions.frameOf(0))
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
            })
            .into(binding.postVideo)


        binding.cancelReelBtn.setOnClickListener {
            goToMainActivity()
        }

        binding.backPress.setOnClickListener {
            goToMainActivity()
        }

        binding.postReelBtn.setOnClickListener {
            val video = Uri.parse(videoUrl)
            ProgressDialog.showDialog(this)
            uploadReels(this, video, Constants.REEL_FOLDER) { videoUrl ->
                if (videoUrl != null) {
                    postYourReel(videoUrl.toString())
                }
            }
        }
    }

    private fun postYourReel(videoUrl: String) {
        ProgressDialog.showDialog(this)
        postViewModel.postYourReel(
            videoUrl,
            binding.captionReelET.editableText.toString(),
            onSuccess = {
                ProgressDialog.hideDialog()
                goToMainActivity()
            },
            onError = {
                showToast(it)
            }
        )
    }


    override fun defineLayout(): Int {
        return R.layout.activity_add_reel
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityAddReelBinding
    }

}