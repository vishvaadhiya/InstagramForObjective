package com.example.instagramforobjective.ui.profileManagement.fragment

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instagramforobjective.base.BaseFragment
import com.example.instagramforobjective.ui.profileManagement.adapter.UserPostRvAdapter
import com.example.instagramforobjective.ui.profileManagement.shared.UserViewModel
import com.example.instagramforobjective.data.models.Post
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.FragmentUserPostBinding


class UserPostFragment : BaseFragment() {

    lateinit var binding: FragmentUserPostBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun defineLayout(): Int {
        return R.layout.fragment_user_post
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentUserPostBinding
        return this.binding
    }

    override fun initComponent() {
        loadPostData()
    }

    private fun loadPostData() {
        val postList = ArrayList<Post>()
        val adapter = UserPostRvAdapter(requireContext(), postList)
        binding.userPostRV.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        binding.userPostRV.isNestedScrollingEnabled = false
        binding.userPostRV.adapter = adapter

        userViewModel.fetchUserPost()
        userViewModel.postsLiveData.observe(viewLifecycleOwner) { postList ->
            adapter.updateData(postList)
        }
    }
}