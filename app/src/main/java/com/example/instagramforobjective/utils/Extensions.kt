package com.example.instagramforobjective.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.instagramforobjective.ui.homeModule.HomeActivity
import com.example.instagramforobjective.utils.customViews.ProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun uploadImage(context: Context,uri: Uri?, folderName: String, callback: (String?) -> Unit) {
    uri?.let { imageUri ->
        ProgressDialog.getInstance(context).show()
        FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
            .putFile(imageUri)
            .addOnSuccessListener { uploadTask ->
                uploadTask.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                    ProgressDialog.getInstance(context).hide()
                    val imageUrl = downloadUri.toString()
                    callback(imageUrl)
                }.addOnFailureListener { exception ->
                    callback(null)
                    Log.e("TAG", "Failed to get download URL: ${exception.message}")
                }
            }
            .addOnFailureListener { exception ->
                callback(null)
                Log.e("TAG", "Failed to upload image: ${exception.message}")
            }
    } ?: run {
        callback(null)
        Log.e("TAG", "URI is null.")
    }
}

fun getCurrentUserId(): String? {
    val currentUser = FirebaseAuth.getInstance().currentUser
    return currentUser?.uid
}


fun Context.goToMainActivity() {
    Intent(this, HomeActivity::class.java).also {
        startActivity(it)
    }
}

fun uploadReels(context: Context,uri: Uri, folderName: String, callback: (String?) -> Unit) {
    var imageUri: String? = null
    ProgressDialog.getInstance(context).show()
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            ProgressDialog.getInstance(context).hide()
            it.storage.downloadUrl.addOnSuccessListener {
                imageUri = it.toString()
                Log.d("TAG", "uploadReels: ${imageUri!!.length} ")
                callback(imageUri)
            }
        }

        .addOnProgressListener {
            val uploadVideo: Long = (it.bytesTransferred / it.totalByteCount) * 100
            Log.d("TAG", "uploadReels: uploadVideo $uploadVideo")
        }

}
