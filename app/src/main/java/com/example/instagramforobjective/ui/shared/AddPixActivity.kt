package com.example.instagramforobjective.ui.shared

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.instagramforobjective.ui.postManagement.addPost.PostActivity
import com.example.instagramforobjective.ui.reelManagement.addReel.AddReelActivity
import com.example.instagramforobjective.ui.postManagement.addStory.StoryActivity
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.utils.customViews.fragmentBody
import com.example.instagramforobjective.utils.showToast
import com.example.instagramforobjective.utils.uploadImage
import com.example.instagramforobjective.utils.uploadReels
import com.example.instagramforobjective.R
import com.iceteck.silicompressorr.SiliCompressor
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import java.io.File
import java.net.URISyntaxException

class AddPixActivity : AppCompatActivity() {

    var options = Options()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pix)
        options.isFrontFacing = true
        showCameraFragment()

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
            Log.d("TAG", "Unexpected permission request code")
        }
    }


    private fun showCameraFragment() {
        val source = intent.getStringExtra(Constants.SOURCE)
        if (source == Constants.VIDEO){
            options.mode = Mode.Video
        }else{
            options.mode = Mode.Picture
        }
        addPixToActivity(R.id.container, options) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    if (source == Constants.VIDEO){
                        val videoUris = result.data as ArrayList<Uri>
                        if (videoUris.isNotEmpty()) {
                            val videoUri = videoUris[0]
                            val file = File(Environment.getExternalStorageDirectory().absolutePath)
                            CompressVideo().execute("false",videoUri.toString(),file.path)
                            Log.d("TAG", "showCameraFragment: selected Video Uri $videoUri ")
                        } else {
                            Toast.makeText(this,
                                getString(R.string.no_images_selected), Toast.LENGTH_SHORT).show()
                        }
                    }else if (source == Constants.SOURCE_STORY){
                        val imageUris = result.data as ArrayList<Uri>
                        if (imageUris.isNotEmpty()) {
                            val imageUri = imageUris[0]
                            uploadImage(this,imageUri, Constants.STORY_FOLDER) { imageUrl ->
                                if (imageUrl != null) {
                                    val intent = Intent(this, StoryActivity::class.java)
                                    intent.putExtra(Constants.IMAGE_URI, imageUrl)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this,
                                        getString(R.string.failed_to_upload_image), Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.no_images_selected), Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        val imageUris = result.data as ArrayList<Uri>
                        if (imageUris.isNotEmpty()) {
                            val imageUri = imageUris[0]
                            uploadImage(this,imageUri, Constants.POST_FOLDER) { imageUrl ->
                                if (imageUrl != null) {
                                    val intent = Intent(this, PostActivity::class.java)
                                    intent.putExtra(Constants.IMAGE_URI, imageUrl)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this,  getString(R.string.failed_to_upload_image), Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.no_images_selected), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                PixEventCallback.Status.BACK_PRESSED -> {
                    supportFragmentManager.popBackStack()
                }
            }
        }


    }

    @SuppressLint("StaticFieldLeak")
    private inner class CompressVideo : AsyncTask<String?, Uri?, String?>() {
        var dialog: Dialog? = null
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            dialog = ProgressDialog.show(
                this@AddPixActivity, "", "Compressing..."
            )
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: String?): String? {
            var videoPath: String? = null
            try {

                val uri = Uri.parse(p0[1])

                videoPath = SiliCompressor.with(this@AddPixActivity)
                    .compressVideo(uri, p0[2])
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }

            return videoPath
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            dialog!!.dismiss()

            onCompressionComplete(s)
        }
    }

    fun onCompressionComplete(compressedVideoPath: String?) {
        if (compressedVideoPath != null) {
            val compressedVideoFile = File(compressedVideoPath)
            val compressedVideoUri = FileProvider.getUriForFile(this, "${packageName}.provider", compressedVideoFile)
            uploadReels(this,compressedVideoUri, Constants.REEL_FOLDER) { videoUrl ->
                if (videoUrl != null) {
                    val intent = Intent(this, AddReelActivity::class.java)
                    intent.putExtra(Constants.VIDEO_URI, videoUrl)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, getString(R.string.failed_to_upload_video), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Failed to compress video", Toast.LENGTH_SHORT).show()
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

