package com.example.instagramforobjective.ui.shared

import android.Manifest
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
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.instagramforobjective.ui.post.addPost.PostActivity
import com.example.instagramforobjective.ui.reel.addReel.AddReelActivity
import com.example.instagramforobjective.ui.post.addStory.StoryActivity
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.utils.customViews.fragmentBody
import com.example.instagramforobjective.utils.showToast
import com.example.instagramforobjective.R
import com.iceteck.silicompressorr.SiliCompressor
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import java.io.File
import java.net.URISyntaxException

class AddPixActivity : AppCompatActivity() {

    private var options = Options()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pix)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                100
            )
        } else {
            showCameraFragment()
        }

        options.isFrontFacing = true
/*        options.isFrontFacing = true
        showCameraFragment()*/

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCameraFragment()
            } else {
                showToast("Camera permission denied")
            }
        }
    }


    private fun showCameraFragment() {
        val source = intent.getStringExtra(Constants.SOURCE)
        if (source == Constants.VIDEO){
            options.mode = Mode.Video
        }else{
            options.mode = Mode.Picture
        }
        addPixToActivity(R.id.container_pix, options) { result ->
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
                            com.example.instagramforobjective.utils.customViews.ProgressDialog.showDialog(this)
                            val intent = Intent(this, StoryActivity::class.java)
                            intent.putExtra(Constants.IMAGE_URI, imageUri.toString())
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, getString(R.string.no_images_selected), Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        val imageUris = result.data as ArrayList<Uri>
                        if (imageUris.isNotEmpty()) {
                            val imageUri = imageUris[0]
                            val intent = Intent(this, PostActivity::class.java)
                            intent.putExtra(Constants.IMAGE_URI, imageUri.toString())
                            startActivity(intent)
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
            val intent = Intent(this, AddReelActivity::class.java)
            intent.putExtra(Constants.VIDEO_URI, compressedVideoUri.toString())
            startActivity(intent)
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

