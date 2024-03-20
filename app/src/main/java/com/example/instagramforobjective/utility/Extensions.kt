package com.example.instagramforobjective.utility

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.instagramforobjective.R
import com.example.instagramforobjective.ui.dashboard.MainActivity
import com.example.instagramforobjective.ui.model.UserModel
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.UUID

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun uploadImage(uri: Uri, folderName: String, callback: (String?) -> Unit) {

    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener { it ->
            it.storage.downloadUrl.addOnSuccessListener {
                val imageUri = it.toString()
                callback(imageUri)
            }.addOnFailureListener {
                it.printStackTrace()
                Log.d("TAG", "exce ${it.message}")
            }
        }

}


fun Context.goToMainActivity() {
    Intent(this, MainActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun uploadReels(uri: Uri, folderName: String, callback: (String?) -> Unit) {
    var imageUri: String? = null
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imageUri = it.toString()
                callback(imageUri)
            }
        }

        .addOnProgressListener {
            val uploadVideo: Long = (it.bytesTransferred / it.totalByteCount) * 100
        }

}

//@SuppressLint("CheckResult", "UseCompatLoadingForDrawables")
//@BindingAdapter(
//    value = ["imagePath", "circleCrop"],
//    requireAll = false
//)
//fun loadImage(
//    view: AppCompatImageView,
//    imagePath: String? = null,
//    circleCrop: Boolean = false,
//) {
//                Glide.with(view).load(imagePath).apply{
//                    if (circleCrop)
//                        circleCrop()
//
//                }
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.user)
//                .error(R.drawable.user)
//                .into(view)
//}
