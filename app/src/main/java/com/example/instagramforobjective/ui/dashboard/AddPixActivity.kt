package com.example.instagramforobjective.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.instagramforobjective.R
import com.example.instagramforobjective.ui.dashboard.custom.fragmentBody
import com.example.instagramforobjective.ui.post.PostActivity
import com.example.instagramforobjective.ui.post.ReelsActivity
import com.example.instagramforobjective.ui.post.StoryActivity
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.ProgressDialog
import com.example.instagramforobjective.utility.showToast
import com.example.instagramforobjective.utility.uploadImage
import com.example.instagramforobjective.utility.uploadReels
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options

class AddPixActivity : AppCompatActivity() {

    var options = Options()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pix)
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.READ_MEDIA_VIDEO
                    ), 0
                )

                showToast("test.....")
            }
        }else{
            showCameraFragment()
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0){
            var allPermissionsGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }
            if (allPermissionsGranted) {
                showCameraFragment()
            } else {
                showToast("Permission denied")
            }
        } else {
            showToast("Unexpected permission request code")
        }
    }


    private fun showCameraFragment() {
        val source = intent.getStringExtra("source")
        if (source == "reel"){
            options.mode = Mode.Video
        }else{
            options.mode = Mode.Picture
        }
        options.isFrontFacing = true
        addPixToActivity(R.id.container, options) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    if (source == "reel"){
                        val videoUris = result.data as ArrayList<Uri>
                        if (videoUris.isNotEmpty()) {
                            val videoUri = videoUris[0]
                            uploadReels(videoUri, Constants.REEL_FOLDER) { videoUrl ->
                                if (videoUrl != null) {
                                    val intent = Intent(this, ReelsActivity::class.java)
                                    intent.putExtra("videoUri", videoUrl)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "Failed to upload video", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show()
                        }
                    }else if (source == "story"){
                        val imageUris = result.data as ArrayList<Uri>
                        if (imageUris.isNotEmpty()) {
                            val imageUri = imageUris[0]
                            uploadImage(imageUri, Constants.STORY_FOLDER) { imageUrl ->
                                if (imageUrl != null) {
                                    val intent = Intent(this, StoryActivity::class.java)
                                    intent.putExtra("imageUri", imageUrl)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        val imageUris = result.data as ArrayList<Uri>
                        if (imageUris.isNotEmpty()) {
                            val imageUri = imageUris[0]
                            uploadImage(imageUri, Constants.POST_FOLDER) { imageUrl ->
                                if (imageUrl != null) {
                                    val intent = Intent(this, PostActivity::class.java)
                                    intent.putExtra("imageUri", imageUrl)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                PixEventCallback.Status.BACK_PRESSED -> {
                    supportFragmentManager.popBackStack()
                }
            }
        }


    }
}


class ResultsFragment(private val clickCallback: View.OnClickListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = fragmentBody(requireActivity(), clickCallback)
}

