package com.example.instagramforobjective.ui.post


import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivityPostBinding
import com.example.instagramforobjective.ui.model.Post
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.ProgressDialog
import com.example.instagramforobjective.utility.goToMainActivity
import com.example.instagramforobjective.utility.showToast
import com.example.instagramforobjective.utility.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class PostActivity : BaseActivity() {

    lateinit var binding: ActivityPostBinding
    private var imageUrl: String? = null

    override fun initComponents() {
        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                ProgressDialog.showDialog(this)
                uploadImage(uri, Constants.POST_FOLDER) { url ->
                    if (url != null) {
                        imageUrl = url
                        ProgressDialog.hideDialog()
                        binding.postImage.setImageURI(uri)
                    }
                }
            }
        }
        binding.postImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.cancelBtn.setOnClickListener {
            goToMainActivity()
        }
        binding.postBtn.setOnClickListener {
            postYourData()
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