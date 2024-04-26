package com.example.instagram.ui.searchModule

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagram.base.BaseFragment
import com.example.instagram.ui.profileManagement.adapter.UserPostRvAdapter
import com.example.instagram.data.models.Post
import com.example.instagram.data.models.User
import com.example.instagram.utils.Constants
import com.example.instagram.utils.customViews.ProgressDialog
import com.example.instagram.utils.showToast
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.FragmentSearchBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class SearchFragment : BaseFragment(), SearchAdapter.OnFollowButtonClickListener {

    private val progressDialog by lazy {
        ProgressDialog.getInstance(requireContext())
    }
    lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchAdapter
    private var userList = ArrayList<User>()
    private val searchViewModel: SearchViewModel by viewModels()

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
//        adapter = SearchAdapter(requireContext(), userList, this)
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
//        ProgressDialog.showDialog(requireActivity() as AppCompatActivity)
        progressDialog.show()
        binding.searchRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(), userList, this)
        binding.searchRv.adapter = adapter
        searchViewModel.fetchUser(onSuccess = {
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
                progressDialog.hide()
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
//        ProgressDialog.showDialog(requireActivity() as AppCompatActivity)
        progressDialog.show()
        val postList = ArrayList<Post>()
        val adapter = UserPostRvAdapter(requireContext(), postList)
        binding.postRv.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.postRv.adapter = adapter

        searchViewModel.fetchPosts(onSuccess = {
//            ProgressDialog.hideDialog()
            progressDialog.hide()
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
                                    userList.firstOrNull {
                                        it.uid == currentUser.uid
                                    }?.apply {
                                        isFollow = true
                                    }
                                    adapter.notifyItemChanged(userIndex)
                                    requireContext().showToast("Followed ${user.name}")
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
                                    userList.firstOrNull {
                                        it.uid == currentUser.uid
                                    }?.apply {
                                        isFollow = false
                                    }
                                    adapter.notifyItemChanged(userIndex)
                                    requireContext().showToast("Unfollowed ${user.name}")
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("TAG", "Error : $exception")
                                }
                        } else {
                            requireContext().showToast("test")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("TAG", "Error : $exception")
                    }
            }
        }
    }
}