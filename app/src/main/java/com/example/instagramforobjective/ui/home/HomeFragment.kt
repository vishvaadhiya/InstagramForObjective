package com.example.instagramforobjective.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramforobjective.base.BaseFragment
import com.example.instagramforobjective.ui.home.adapters.HomeAdapter
import com.example.instagramforobjective.ui.home.adapters.StoryAdapter
import com.example.instagramforobjective.data.models.Post
import com.example.instagramforobjective.data.models.Story
import com.example.instagramforobjective.utils.customViews.ProgressDialog
import com.example.instagramforobjective.utils.helpers.PreferenceHelper
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeAdapter
    private lateinit var storyAdapter: StoryAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private var postList = ArrayList<Post>()
    private var storyList = ArrayList<Story>()
    private val pHelpers by lazy {
        PreferenceHelper(requireContext())
    }


    override fun defineLayout(): Int {
        return R.layout.fragment_home
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentHomeBinding
        return this.binding
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initComponent() {
        ProgressDialog.showDialog(requireActivity())
        val preferenceHelper = PreferenceHelper(requireContext())
        storyAdapter = view?.let { StoryAdapter(it, requireContext(), storyList) }!!
        binding.storyRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.storyRecyclerView.adapter = storyAdapter


        adapter = HomeAdapter(
            requireContext(),
            postList,
            preferenceHelper,
            FirebaseAuth.getInstance().currentUser?.uid
        )
        binding.homeRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.homeRv.adapter = adapter


        homeViewModel.fetchPostsAndStories()
        homeViewModel.postsLiveData.observe(viewLifecycleOwner) { postList ->
            ProgressDialog.hideDialog()
            adapter.updatePosts(postList)
            loadLikeStates()
        }
        homeViewModel.storiesLiveData.observe(viewLifecycleOwner) { storyList ->
            ProgressDialog.hideDialog()
            Log.d("TAG", "initComponent: ${storyList.size}")
            storyAdapter.updatePosts(storyList)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun loadLikeStates() {
        postList.forEach { post ->
            val isLiked =
                pHelpers.loadLikeState(post.postId, FirebaseAuth.getInstance().currentUser!!.uid)
            post.isLikedImage = isLiked

            val isSaved = pHelpers.loadSavedState(post.postId, FirebaseAuth.getInstance().currentUser!!.uid)
            post.isSavedImage = isSaved
        }

        adapter.notifyDataSetChanged()
    }
}