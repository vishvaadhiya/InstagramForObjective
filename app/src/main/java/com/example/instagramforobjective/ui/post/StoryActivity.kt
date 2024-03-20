package com.example.instagramforobjective.ui.post

import android.app.ProgressDialog
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivityStoryBinding
import com.example.instagramforobjective.ui.model.Story
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.goToMainActivity
import com.example.instagramforobjective.utility.showToast
import com.example.instagramforobjective.utility.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StoryActivity : BaseActivity() {

    lateinit var binding: ActivityStoryBinding
    private lateinit var progressDialog: ProgressDialog
    private var imageUrl: String? = null

    override fun initComponents() {
        progressDialog =  ProgressDialog(this)
        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                com.example.instagramforobjective.utility.ProgressDialog.showDialog(this)
                uploadImage(uri, Constants.STORY_FOLDER) { url ->
                    if (url != null) {
                        imageUrl = url
                        com.example.instagramforobjective.utility.ProgressDialog.hideDialog()
                        binding.storyIv.setImageURI(uri)
                    }
                }
            }
        }
        binding.storyIv.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.cancelStoryBtn.setOnClickListener {
            goToMainActivity()
        }
        binding.postStoryBtn.setOnClickListener {
            postStoryYourData()
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
            val story: Story = Story(
                storyUrl = imageUrl!!,
                uid = FirebaseAuth.getInstance().currentUser!!.uid,
                time = System.currentTimeMillis().toString()
            )

            Firebase.firestore.collection(Constants.STORY).document().set(story)
                .addOnSuccessListener {
                    goToMainActivity()
                /*Firebase.firestore.collection(FirebaseAuth.getInstance().currentUser!!.uid)
                        .document().set(story).addOnSuccessListener {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }*/
                }
        } else {
            showToast(getString(R.string.please_upload_image_first))
        }
    }
}