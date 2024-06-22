package com.example.instagramforobjective.ui.profile.fragment

import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.instagramforobjective.base.BaseFragment
import com.example.instagramforobjective.ui.home.HomeActivity
import com.example.instagramforobjective.ui.profile.shared.UserViewModel
import com.example.instagramforobjective.data.models.User
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.utils.customViews.ProgressDialog
import com.example.instagramforobjective.utils.getCurrentUserId
import com.example.instagramforobjective.utils.showToast
import com.example.instagramforobjective.utils.uploadImage
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.FragmentUpdateProfileBinding


class UpdateProfileFragment : BaseFragment() {

    lateinit var binding: FragmentUpdateProfileBinding
    lateinit var user: User
    val userViewModel: UserViewModel by viewModels()


    override fun defineLayout(): Int {
        return R.layout.fragment_update_profile
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentUpdateProfileBinding
        return this.binding
    }

    override fun initComponent() {
        user = User()
        userViewModel.fetchUserData(onSuccess = {
            user = it

            updateUIWithUserData()
        }, onError = {
            Log.d("TAG", "can't find detail ")
        })

        setUpClickListeners()


    }

    private fun updateUIWithUserData() {
        binding.updateEmailEditText.setText(user.email)
        binding.updateUsernameEditText.setText(user.name)
        binding.updatePasswordEditText.setText(user.password)

        if (user.image.isNotEmpty()) {
            Glide.with(requireActivity())
                .load(user.image)
                .placeholder(R.drawable.user)
                .into(binding.userProfileView)
        }
    }

    private fun setUpClickListeners() {
        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                ProgressDialog.showDialog(requireContext() as AppCompatActivity)
//                ProgressDialog.showDialog(activity as AppCompatActivity)
                uploadImage(requireContext(),uri, Constants.USER_PROFILE) { imageUrl ->
                    imageUrl?.let {
                        user.image = it
                        ProgressDialog.hideDialog()
//                        ProgressDialog.hideDialog()
                        binding.userProfileView.setImageURI(uri)
                    }
                }
            }
        }
        binding.userProfileView.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.editProfileTv.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.updateProfileBtn.setOnClickListener {
            val newEmail = binding.updateEmailEditText.text.toString()
            val newName = binding.updateUsernameEditText.text.toString()
            val newPassword = binding.updatePasswordEditText.text.toString()

            userViewModel.updateUser(newEmail,newName,newPassword, user.image,getCurrentUserId() ?: return@setOnClickListener ,onSuccess = {
                activity?.showToast("Successfully Updated")
                startActivity(Intent(activity, HomeActivity::class.java))
            }, onError = {
                Log.d("TAG", "initComponent: some issue")
            })

        }
    }

}
