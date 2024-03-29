package com.example.instagramforobjective.ui.dashboard

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseFragment
import com.example.instagramforobjective.databinding.FragmentSearchBinding
import com.example.instagramforobjective.ui.dashboard.adapter.SearchAdapter
import com.example.instagramforobjective.ui.dashboard.adapter.UserPostRvAdapter
import com.example.instagramforobjective.ui.model.Post
import com.example.instagramforobjective.ui.model.SavedPost
import com.example.instagramforobjective.ui.model.UserModel
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.ProgressDialog
import com.example.instagramforobjective.utility.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class SearchFragment : BaseFragment() {

    lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchAdapter
    var userList = ArrayList<UserModel>()
    var userDataLoaded = false

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
        binding.searchView.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("TAG", "before text changed")
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (query.isNullOrEmpty()) {
                    binding.postRv.visibility = View.VISIBLE
                    binding.searchRv.visibility = View.GONE
                    loadPostData()
                    userDataLoaded = false
                } else if (query.length >= 2 && !userDataLoaded) {
                    binding.postRv.visibility = View.GONE
                    binding.searchRv.visibility = View.VISIBLE
                    loadUserData()
                    userDataLoaded = true
                } else if (query.length >= 2) {
                    binding.postRv.visibility = View.GONE
                    binding.searchRv.visibility = View.VISIBLE
                    performSearch(query.toString())
                } else {
                    binding.postRv.visibility = View.VISIBLE
                    binding.searchRv.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d("TAG", "after text changed")
            }
        })
    }

    private fun loadUserData() {
        ProgressDialog.showDialog(requireActivity() as AppCompatActivity)
        binding.searchRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(), userList)
        binding.searchRv.adapter = adapter
        Firebase.firestore.collection(Constants.USER).get().addOnSuccessListener {

            var tempList = ArrayList<UserModel>()
            userList.clear()
            for (i in it.documents) {
                var user: UserModel = i.toObject<UserModel>()!!
                if (user.email != FirebaseAuth.getInstance().currentUser?.email) {
                    ProgressDialog.hideDialog()
                    tempList.add(user)
                }

            }

            userList.addAll(tempList)
            adapter.notifyDataSetChanged()

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

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid != null) {
            Firebase.firestore.collection(Constants.POSTS)
                .get()
                .addOnSuccessListener { postSnapshot ->
                    val tempList = arrayListOf<Post>()
                    for (document in postSnapshot.documents) {
                        val post = document.toObject<Post>()
                        if (post != null) {
                            tempList.add(post)
                        }
                    }
                    ProgressDialog.hideDialog()
                    postList.addAll(tempList)
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(activity, "$exception", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.d("TAG", "if not find user")
        }
    }

    private fun performSearch(query: String) {
        filterData(query)
    }

    private fun filterData(query: String) {
        val filteredList: ArrayList<UserModel> = ArrayList()

        for (user in userList) {
            if (user.name?.lowercase()!!.contains(query)) {
                filteredList.add(user)
            }
        }

        if (filteredList.isEmpty()) {
            requireActivity().showToast(getString(R.string.no_data_found))
        } else {
            adapter.updateData(filteredList)
        }
    }


}