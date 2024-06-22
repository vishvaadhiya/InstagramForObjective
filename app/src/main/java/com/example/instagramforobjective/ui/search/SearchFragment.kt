package com.example.instagramforobjective.ui.search

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramforobjective.base.BaseFragment
import com.example.instagramforobjective.ui.profile.adapter.UserPostRvAdapter
import com.example.instagramforobjective.data.models.Post
import com.example.instagramforobjective.data.models.User
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.utils.customViews.ProgressDialog
import com.example.instagramforobjective.utils.showToast
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.FragmentSearchBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class SearchFragment : BaseFragment(), SearchAdapter.OnFollowButtonClickListener {

    lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchAdapter
    private var userList = ArrayList<User>()
    private val repository: SearchRepository by lazy { SearchRepository() }
    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(repository)
    }

    override fun defineLayout(): Int {
        return R.layout.fragment_search
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentSearchBinding
        return this.binding
    }

    override fun initComponent() {
        binding.postRv.visibility = View.VISIBLE
        binding.searchRv.visibility = View.GONE
        loadPostData()
        binding.searchView.searchField.onFocusChangeListener =
            View.OnFocusChangeListener { _, _ ->
                loadUserData()
                binding.postRv.visibility = View.GONE
                binding.searchRv.visibility = View.VISIBLE
            }
        binding.searchView.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("TAG", "before text changed")
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.postRv.visibility = View.GONE
                binding.searchRv.visibility = View.VISIBLE
                performSearch(query.toString())

            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d("TAG", "after text changed")
            }
        })
    }

    private fun loadUserData() {
        ProgressDialog.showDialog(requireActivity() as AppCompatActivity)
        binding.searchRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(userList, this)
        binding.searchRv.adapter = adapter
        searchViewModel.fetchUsers(onSuccess = {
            userList.clear()
            userList.addAll(it)
            for (user in it) {
                fetchFollowStatus(user)
            }
        }, onError = {
            requireContext().showToast(it)
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchFollowStatus(user: User) {
        val followCollection =
            Firebase.firestore.collection(FirebaseAuth.getInstance().currentUser!!.uid + Constants.FOLLOWERS)
        followCollection.whereEqualTo(Constants.EMAIL, user.email).get()
            .addOnSuccessListener { querySnapshot ->
                ProgressDialog.hideDialog()
                val isFollowed = !querySnapshot.isEmpty
                user.isFollow = isFollowed
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Error: $exception")
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPostData() {
        ProgressDialog.showDialog(requireActivity() as AppCompatActivity)
        val postList = ArrayList<Post>()
        val adapter = UserPostRvAdapter(requireContext(), postList)
        binding.postRv.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.postRv.adapter = adapter

        searchViewModel.fetchPosts(onSuccess = {
            ProgressDialog.hideDialog()
            postList.addAll(it)
            adapter.notifyDataSetChanged()
        }, onError = {
            requireContext().showToast(it)
        })
    }

    private fun performSearch(query: String) {
        if (query.isEmpty()) {
            loadUserData()
        } else {
            filterData(query)
        }
    }

    private fun filterData(query: String) {
        val filteredList: ArrayList<User> = ArrayList()

        for (user in userList) {
            if (user.name.lowercase().contains(query.lowercase())) {
                filteredList.add(user)
            }
        }
        adapter.updateData(filteredList)

        if (filteredList.isEmpty()) {
            filteredList.clear()
            requireActivity().showToast(getString(R.string.no_data_found))
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onFollowButtonClicked(user: User, isFollowing: Boolean) {
        val userIndex = userList.indexOf(user)
        if (userIndex != -1) {
            val currentUser = userList[userIndex]
            val followCollection =
                Firebase.firestore.collection(FirebaseAuth.getInstance().currentUser!!.uid + Constants.FOLLOWERS)
            if (isFollowing) {
                followCollection.whereEqualTo(Constants.EMAIL, user.email).get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            followCollection.document().set(user)
                                .addOnSuccessListener {
                                    currentUser.isFollow = true
                                    userList.firstOrNull { it.uid == currentUser.uid }?.isFollow = true
                                    requireContext().showToast("Followed ${user.name}")
                                    adapter.notifyDataSetChanged()
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("TAG", "Error adding follower document: $exception")
                                }
                        } else {
                            requireContext().showToast("user already followed")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("TAG", "Error checking follower document: $exception")
                    }
            } else {
                followCollection.whereEqualTo(Constants.EMAIL, user.email).get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val documentId = querySnapshot.documents[0].id
                            followCollection.document(documentId).delete()
                                .addOnSuccessListener {
                                    currentUser.isFollow = false
                                    userList.firstOrNull { it.uid == currentUser.uid }?.isFollow = false
                                    requireContext().showToast("Unfollowed ${user.name}")
                                    adapter.notifyDataSetChanged()
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("TAG", "Error : $exception")
                                }
                        } else {
                            requireContext().showToast("user not followed")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("TAG", "Error : $exception")
                    }
            }
        }
    }
}