package com.example.instagramforobjective.ui.dashboard

import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseFragment
import com.example.instagramforobjective.databinding.FragmentUpdateProfileBinding


class UpdateProfileFragment : BaseFragment() {

    lateinit var binding: FragmentUpdateProfileBinding

    override fun defineLayout(): Int {
        return R.layout.fragment_update_profile
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentUpdateProfileBinding
        return this.binding
    }

    override fun initComponent() {
        binding.emailEditText.setOnClickListener {
            Toast.makeText(activity, "Test", Toast.LENGTH_SHORT).show()
        }
    }


}