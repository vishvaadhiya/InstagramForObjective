package com.example.instagramforobjective.ui.dashboard

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseFragment
import com.example.instagramforobjective.databinding.FragmentUserPostBinding
import com.example.instagramforobjective.ui.dashboard.adapter.UserPostRvAdapter
import com.example.instagramforobjective.ui.model.Post
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.ProgressDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class UserPostFragment : BaseFragment() {

    lateinit var binding: FragmentUserPostBinding

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
        ProgressDialog.showDialog(requireActivity() as AppCompatActivity)
        val postList = ArrayList<Post>()
        val adapter = UserPostRvAdapter(requireContext(), postList)
        binding.userPostRV.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.userPostRV.adapter = adapter

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid != null) {
            Firebase.firestore.collection(Constants.POSTS)
                .whereEqualTo("uid", currentUserUid)
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
            Log.d("TAG","if not find user")
        }
    }

}