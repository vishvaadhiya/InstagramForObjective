package com.example.instagramforobjective.ui.dashboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.FragmentAddPostBinding
import com.example.instagramforobjective.ui.post.PostActivity
import com.example.instagramforobjective.ui.post.ReelsActivity
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.showToast
import com.example.instagramforobjective.utility.uploadImage
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddPostFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddPostBinding
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            onDismiss(requireDialog())
            if (uri.toString().endsWith(".mp4")){
                val intent = Intent(requireContext(), ReelsActivity::class.java)
                intent.putExtra(Constants.VIDEO_URI, uri.toString())
                activity?.startActivity(intent)
            }else{
                uploadImage(uri, Constants.POST_FOLDER) { imageUrl ->
                    if (imageUrl != null) {
                        val intent = Intent(requireContext(), PostActivity::class.java)
                        intent.putExtra(Constants.IMAGE_URI, imageUrl)
                        startActivity(intent)
                    } else {
                        requireContext().showToast(getString(R.string.failed_to_upload_image))
                    }
                }
            }
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        binding.postCL.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                activity?.startActivity(Intent(requireContext(), AddPixActivity::class.java))
                dismiss()
            }
        }
        binding.reelCL.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
            }else{
                val intent = Intent(requireContext(), AddPixActivity::class.java)
                intent.putExtra(Constants.SOURCE, Constants.VIDEO)
                activity?.startActivity(intent)
                dismiss()
            }
        }
        return binding.root
    }
}