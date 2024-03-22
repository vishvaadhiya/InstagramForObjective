package com.example.instagramforobjective.ui.dashboard

import android.util.Log
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseFragment
import com.example.instagramforobjective.databinding.FragmentReelBinding
import com.example.instagramforobjective.ui.dashboard.adapter.ReelAdapter
import com.example.instagramforobjective.ui.model.Reel
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.ProgressDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ReelFragment : BaseFragment() {

    lateinit var binding: FragmentReelBinding
    private lateinit var adapter: ReelAdapter
    var reelList = ArrayList<Reel>()

    override fun defineLayout(): Int {
        return R.layout.fragment_reel
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentReelBinding
        return this.binding
    }

    override fun initComponent() {
        adapter = ReelAdapter(requireContext(), reelList)
        binding.reelRv.layoutManager = LinearLayoutManager(requireContext())
        binding.reelRv.adapter = adapter

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
                    Toast.makeText(requireContext(), "${tempList.size}", Toast.LENGTH_SHORT).show()
                    Log.d("TAG","tempList size ${tempList.size}")
                    ProgressDialog.hideDialog()
                    reelList.addAll(tempList)
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(activity, "$exception", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.d("TAG", "if not find user data")
        }
    }

}
