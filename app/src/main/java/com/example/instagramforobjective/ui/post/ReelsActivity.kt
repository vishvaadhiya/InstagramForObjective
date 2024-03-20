package com.example.instagramforobjective.ui.post

import android.app.ProgressDialog
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivityReelsBinding
import com.example.instagramforobjective.ui.dashboard.MainActivity
import com.example.instagramforobjective.ui.model.Reel
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.goToMainActivity
import com.example.instagramforobjective.utility.uploadReels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReelsActivity : BaseActivity() {

    lateinit var binding: ActivityReelsBinding
    private lateinit var progressDialog: ProgressDialog
    private var videoUrl: String? = null

    override fun initComponents() {
        progressDialog =  ProgressDialog(this)
        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                com.example.instagramforobjective.utility.ProgressDialog.showDialog(this)
                uploadReels(uri, Constants.REEL_FOLDER) { url ->
                    if (url != null) {
                        videoUrl = url
                        com.example.instagramforobjective.utility.ProgressDialog.hideDialog()
                        binding.postVideo.setImageURI(uri)
                    }
                }
            }
        }
        binding.postVideo.setOnClickListener {
            launcher.launch("video/*")
        }
        binding.cancelReelBtn.setOnClickListener {
            goToMainActivity()
        }
        binding.postReelBtn.setOnClickListener {
            val reel: Reel = Reel(videoUrl!!, binding.captionReelET.editableText.toString(),FirebaseAuth.getInstance().currentUser!!.uid)
            Firebase.firestore.collection(Constants.REEL).document().set(reel).addOnSuccessListener {
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