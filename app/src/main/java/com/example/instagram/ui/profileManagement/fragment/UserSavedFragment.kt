package com.example.instagram.ui.profileManagement.fragment

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagram.base.BaseFragment
import com.example.instagram.ui.profileManagement.shared.UserViewModel
import com.example.instagram.ui.profileManagement.adapter.UserSavedRvAdapter
import com.example.instagram.data.models.SavedPost
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.FragmentUserSavedBinding


class UserSavedFragment : BaseFragment() {

    lateinit var binding: FragmentUserSavedBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun defineLayout(): Int {
        return R.layout.fragment_user_saved
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentUserSavedBinding
        return this.binding
    }

    override fun initComponent() {
        loadSavedPostData()
    }

    private fun loadSavedPostData() {
        val savedPostList = ArrayList<SavedPost>()
        val adapter = UserSavedRvAdapter(requireContext(), savedPostList)
        binding.userSavedRV.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.userSavedRV.adapter = adapter

        userViewModel.fetchSavedPost()
        userViewModel.savedPostLiveData.observe(viewLifecycleOwner) { savedPostList ->
            adapter.updateSavedPost(savedPostList)
        }


    }
}

