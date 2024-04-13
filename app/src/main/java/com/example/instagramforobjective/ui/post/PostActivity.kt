package com.example.instagramforobjective.ui.post


import android.graphics.drawable.Drawable
import android.widget.Toast
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
import com.example.instagramforobjective.databinding.ActivityPostBinding
import com.example.instagramforobjective.ui.model.Post
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.ProgressDialog
import com.example.instagramforobjective.utility.goToMainActivity
import com.example.instagramforobjective.utility.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class PostActivity : BaseActivity() {

    lateinit var binding: ActivityPostBinding
    private var imageUrl: String? = null

    override fun initComponents() {
        ProgressDialog.showDialog(this as AppCompatActivity)
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
            Toast.makeText(this, "Image URL is null or empty", Toast.LENGTH_SHORT).show()
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
        if (imageUrl != null) {
            val postId = UUID.randomUUID().toString()
            val post: Post = Post(
                postId = postId,
                postUrl = imageUrl!!,
                caption = binding.captionET.editableText.toString(),
                uid = FirebaseAuth.getInstance().currentUser!!.uid,
                time = System.currentTimeMillis().toString(),
                false
            )

            Firebase.firestore.collection(Constants.POSTS).document().set(post)
                .addOnSuccessListener {
                    goToMainActivity()
                }
        } else {
            showToast(getString(R.string.please_upload_image_first))
        }
    }

}