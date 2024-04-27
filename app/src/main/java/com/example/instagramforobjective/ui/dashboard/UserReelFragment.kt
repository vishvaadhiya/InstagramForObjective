package com.example.instagramforobjective.ui.dashboard

import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseFragment
import com.example.instagramforobjective.databinding.FragmentUserReelBinding
import com.example.instagramforobjective.ui.dashboard.adapter.UserPostRvAdapter
import com.example.instagramforobjective.ui.dashboard.adapter.UserReelRvAdapter
import com.example.instagramforobjective.ui.model.Post
import com.example.instagramforobjective.ui.model.Reel
import com.example.instagramforobjective.utility.Constants
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class UserReelFragment : BaseFragment() {

    lateinit var binding: FragmentUserReelBinding
    private lateinit var progressDialog: ProgressDialog

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
        com.example.instagramforobjective.utility.ProgressDialog.showDialog(requireActivity() as AppCompatActivity)
        val reelList = ArrayList<Reel>()
        val adapter = UserReelRvAdapter(requireContext(), reelList)
        binding.userReelRV.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.userReelRV.adapter = adapter

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid != null) {
            Firebase.firestore.collection(Constants.REEL)
                .whereEqualTo("uid", currentUserUid)
                .get()
                .addOnSuccessListener { postSnapshot ->
                    val tempList = arrayListOf<Reel>()
                    for (document in postSnapshot.documents) {
                        val reel = document.toObject<Reel>()
                        if (reel != null) {
                            tempList.add(reel)
                        }
                    }
                    com.example.instagramforobjective.utility.ProgressDialog.hideDialog()
                    reelList.addAll(tempList)
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(activity, "$exception", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.d("TAG","if not find user data")
        }
    }



}