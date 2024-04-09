package com.example.instagramforobjective.ui.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.SearchItemListBinding
import com.example.instagramforobjective.ui.model.UserModel
import com.example.instagramforobjective.utility.Constants
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class SearchAdapter(var context: Context, var userList: ArrayList<UserModel>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: SearchItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = SearchItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isFollow = false
        holder.binding.searchNameTv.text = userList.get(position).name
        Glide.with(context)
            .load(userList[position].image)
            .placeholder(R.drawable.user)
            .circleCrop()
            .into(holder.binding.userProfileIv)
        Firebase.firestore.collection(FirebaseAuth.getInstance().currentUser!!.uid + Constants.FOLLOWERS)
            .whereEqualTo("email", userList.get(position).email).get().addOnSuccessListener {
                if (it.documents.size == 0) {
                    isFollow = false

                } else {
                    holder.binding.followButton.text = context.getString(R.string.unfollow)
                    isFollow = true
                }

            }
        holder.binding.followButton.setOnClickListener {
            if (isFollow) {
                Firebase.firestore.collection(FirebaseAuth.getInstance().currentUser!!.uid + Constants.FOLLOWERS)
                    .whereEqualTo("email", userList.get(position).email).get()
                    .addOnSuccessListener {

                        Firebase.firestore.collection(FirebaseAuth.getInstance().currentUser!!.uid + Constants.FOLLOWERS)
                            .document(it.documents.get(0).id).delete()
                        holder.binding.followButton.text = context.getString(R.string.follow)
                        isFollow = false
                    }
            } else {
                Firebase.firestore.collection(FirebaseAuth.getInstance().currentUser!!.uid + Constants.FOLLOWERS)
                    .document()
                    .set(userList.get(position))
                holder.binding.followButton.text = context.getString(R.string.unfollow)
                isFollow = true
            }


        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: ArrayList<UserModel>) {
        userList = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}