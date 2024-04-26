package com.example.instagram.ui.homeModule

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagram.base.BaseFragment
import com.example.instagram.ui.homeModule.adapters.HomeAdapter
import com.example.instagram.ui.homeModule.adapters.StoryAdapter
import com.example.instagram.data.models.Post
import com.example.instagram.data.models.Story
import com.example.instagram.utils.customViews.ProgressDialog
import com.example.instagram.utils.helpers.PreferenceHelper
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : BaseFragment() {

    private val progressDialog by lazy {
        ProgressDialog.getInstance(requireContext())
    }
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
        progressDialog.show()
//        ProgressDialog.showDialog(requireActivity() as AppCompatActivity)
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
            progressDialog.hide()
//            ProgressDialog.hideDialog()
            adapter.updatePosts(postList)
            loadLikeStates()
        }
        homeViewModel.storiesLiveData.observe(viewLifecycleOwner) { storyList ->
            progressDialog.hide()
//            ProgressDialog.hideDialog()
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
        }

        adapter.notifyDataSetChanged()
    }
}