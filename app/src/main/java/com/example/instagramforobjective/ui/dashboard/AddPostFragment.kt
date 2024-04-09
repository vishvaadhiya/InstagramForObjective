package com.example.instagramforobjective.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagramforobjective.databinding.FragmentAddPostBinding
import com.example.instagramforobjective.ui.post.ReelsActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddPostFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        binding.postCL.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), AddPixActivity::class.java))
            dismiss()
        }
        binding.reelCL.setOnClickListener {
            val intent = Intent(requireContext(), AddPixActivity::class.java)
            intent.putExtra("source", "reel")
            activity?.startActivity(intent)
            dismiss()
        }
        return binding.root
    }

}