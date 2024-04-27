package com.example.instagramforobjective.ui.dashboard

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseFragment
import com.example.instagramforobjective.databinding.FragmentUserSavedBinding
import com.example.instagramforobjective.ui.dashboard.adapter.UserSavedRvAdapter
import com.example.instagramforobjective.ui.model.SavedPost
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.ProgressDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class UserSavedFragment : BaseFragment() {

    lateinit var binding: FragmentUserSavedBinding

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
        ProgressDialog.showDialog(requireActivity() as AppCompatActivity)
        val savedPostList = ArrayList<SavedPost>()
        val adapter = UserSavedRvAdapter(requireContext(), savedPostList)
        binding.userSavedRV.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.userSavedRV.adapter = adapter

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid != null) {
            Firebase.firestore.collection(Constants.SAVED_POST)
                .get()
                .addOnSuccessListener { postSnapshot ->
                    val tempList = arrayListOf<SavedPost>()
                    for (document in postSnapshot.documents) {
                        val post = document.toObject<SavedPost>()
                        if (post != null) {
                            tempList.add(post)
                        }
                    }
                    ProgressDialog.hideDialog()
                    savedPostList.addAll(tempList)
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