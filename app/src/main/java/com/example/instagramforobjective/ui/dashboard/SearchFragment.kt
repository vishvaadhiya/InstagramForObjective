package com.example.instagramforobjective.ui.dashboard

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseFragment
import com.example.instagramforobjective.databinding.FragmentSearchBinding
import com.example.instagramforobjective.ui.dashboard.adapter.SearchAdapter
import com.example.instagramforobjective.ui.model.UserModel
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class SearchFragment : BaseFragment() {

    lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchAdapter
    var userList = ArrayList<UserModel>()

    override fun defineLayout(): Int {
        return R.layout.fragment_search
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentSearchBinding
        return this.binding
    }

    override fun initComponent() {
        binding.searchRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(), userList)
        binding.searchRv.adapter = adapter
        Firebase.firestore.collection(Constants.USER).get().addOnSuccessListener {

            var tempList = ArrayList<UserModel>()
            userList.clear()
            for (i in it.documents) {
                var user: UserModel = i.toObject<UserModel>()!!
                if (user.email != FirebaseAuth.getInstance().currentUser?.email) {
                    tempList.add(user)
                }

            }

            userList.addAll(tempList)
            adapter.notifyDataSetChanged()

        }
        binding.searchView.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("TAG", "before text changed")
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                performSearch(query.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d("TAG", "after text changed")
            }
        })
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