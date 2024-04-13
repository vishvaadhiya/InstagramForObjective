package com.example.instagramforobjective.ui.post

import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivityReelsBinding
import com.example.instagramforobjective.ui.dashboard.MainActivity
import com.example.instagramforobjective.ui.model.Reel
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.ProgressDialog
import com.example.instagramforobjective.utility.goToMainActivity
import com.example.instagramforobjective.utility.uploadReels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReelsActivity : BaseActivity() {

    lateinit var binding: ActivityReelsBinding
    private var videoUrl: String? = null

    override fun initComponents() {
        ProgressDialog.showDialog(this as AppCompatActivity)
        videoUrl = intent.getStringExtra(Constants.VIDEO_URI)
        Glide.with(this)
            .load(videoUrl)
            .apply(RequestOptions.frameOf(0))
            .listener(object:RequestListener<Drawable>{
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
            val reel: Reel = Reel(
                videoUrl!!,
                binding.captionReelET.editableText.toString(),
                FirebaseAuth.getInstance().currentUser!!.uid)
            Firebase.firestore.collection(Constants.REEL).document().set(reel)
                .addOnSuccessListener {
                    startActivity(Intent(this, MainActivity::class.java))
                }
        }
    }

    override fun defineLayout(): Int {
        return R.layout.activity_reels
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityReelsBinding
    }

}