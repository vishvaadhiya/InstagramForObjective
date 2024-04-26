package com.example.instagram.ui.reelManagement.addReel

import android.graphics.drawable.Drawable
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.instagram.base.BaseActivity
import com.example.instagram.ui.postManagement.shared.viewModels.PostViewModel
import com.example.instagram.utils.Constants
import com.example.instagram.utils.customViews.ProgressDialog
import com.example.instagram.utils.goToMainActivity
import com.example.instagram.utils.showToast
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.ActivityAddReelBinding

class AddReelActivity : BaseActivity() {

    lateinit var binding: ActivityAddReelBinding
    private var videoUrl: String? = null
    private val postViewModel: PostViewModel by viewModels()

    override fun initComponents() {
//        ProgressDialog.showDialog(this as AppCompatActivity)
        ProgressDialog.getInstance(this).show()
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
                    ProgressDialog.getInstance(this@AddReelActivity).hide()
//                    ProgressDialog.hideDialog()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    ProgressDialog.getInstance(this@AddReelActivity).hide()
//                    ProgressDialog.hideDialog()
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
            postYourReel(videoUrl.toString())
        }
    }

    private fun postYourReel(videoUrl : String){
        postViewModel.postYourReel(
            videoUrl,
            binding.captionReelET.editableText.toString(),
            onSuccess = {
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

/* if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
               val video = Uri.parse(videoUrl)
               Log.d("TAG", "initComponents video: $video ")
               uploadReels(this,video,Constants.REEL_FOLDER){
                   Log.d("TAG", "initComponents it: $it ")
                   postYourReel(it.toString())
               }
           }else{
           }*/