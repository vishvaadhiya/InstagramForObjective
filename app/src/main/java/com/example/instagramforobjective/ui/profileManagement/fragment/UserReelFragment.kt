package com.example.instagramforobjective.ui.profileManagement.fragment

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramforobjective.base.BaseFragment
import com.example.instagramforobjective.ui.profileManagement.adapter.UserReelRvAdapter
import com.example.instagramforobjective.ui.profileManagement.shared.UserViewModel
import com.example.instagramforobjective.data.models.Reel
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.FragmentUserReelBinding

class UserReelFragment : BaseFragment() {

    lateinit var binding: FragmentUserReelBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun defineLayout(): Int {
        return R.layout.fragment_user_reel
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentUserReelBinding
        return binding
    }

    override fun initComponent() {
        loadReelData()
    }

    private fun loadReelData() {
        val reelList = ArrayList<Reel>()
        val adapter = UserReelRvAdapter(requireContext(), reelList)
        binding.userReelRV.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.userReelRV.adapter = adapter

        userViewModel.fetchUserReel()
        userViewModel.reelLiveData.observe(viewLifecycleOwner) { reelList ->
            adapter.updateReel(reelList)
        }


    }
}