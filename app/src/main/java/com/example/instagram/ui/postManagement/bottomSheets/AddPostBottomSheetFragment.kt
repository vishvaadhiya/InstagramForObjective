package com.example.instagram.ui.postManagement.bottomSheets

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagram.ui.shared.AddPixActivity
import com.example.instagram.ui.postManagement.addPost.PostActivity
import com.example.instagram.ui.reelManagement.addReel.AddReelActivity
import com.example.instagram.utils.Constants
import com.example.instagramforobjective.databinding.BottomSheetFragmentAddPostBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddPostBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetFragmentAddPostBinding
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            onDismiss(requireDialog())
            //val realPath = getRealPathFromURI(uri)
            val contentUri = Uri.parse(uri.toString())
            val contentResolver = requireContext().contentResolver
            //Log.d("TAG", "Real path: $realPath")
            if (isVideoUri(contentResolver,contentUri)) {
                val intent = Intent(requireContext(), AddReelActivity::class.java)
                intent.putExtra(Constants.VIDEO_URI, contentUri)
                startActivity(intent)
            }else{
                Log.d("TAG", "selected uri photos : $uri ")
                val intent = Intent(activity, PostActivity::class.java)
                intent.putExtra(Constants.IMAGE_URI, uri.toString())
                startActivity(intent)
            }
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private fun isVideoUri(contentResolver: ContentResolver, uri: Uri): Boolean {
        val mimeType = contentResolver.getType(uri)
        return mimeType?.startsWith("video/") == true
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        var realPath: String? = null
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = requireActivity().contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            realPath = it.getString(columnIndex)
        }
        return realPath
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BottomSheetFragmentAddPostBinding.inflate(inflater, container, false)
        binding.postCL.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), AddPixActivity::class.java))
            dismiss()
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {

            }*/
        }
        binding.reelCL.setOnClickListener {
            val intent = Intent(requireContext(), AddPixActivity::class.java)
            intent.putExtra(Constants.SOURCE, Constants.VIDEO)
            activity?.startActivity(intent)
            dismiss()
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
            }else{

            }*/
        }
        return binding.root
    }
}