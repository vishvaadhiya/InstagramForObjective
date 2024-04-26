package com.example.instagram.ui.profileManagement.fragment

import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.instagram.base.BaseFragment
import com.example.instagram.ui.homeModule.HomeActivity
import com.example.instagram.ui.profileManagement.shared.UserViewModel
import com.example.instagram.data.models.User
import com.example.instagram.utils.Constants
import com.example.instagram.utils.customViews.ProgressDialog
import com.example.instagram.utils.getCurrentUserId
import com.example.instagram.utils.showToast
import com.example.instagram.utils.uploadImage
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
                ProgressDialog.getInstance(requireContext()).show()
//                ProgressDialog.showDialog(activity as AppCompatActivity)
                uploadImage(requireContext(),uri, Constants.USER_PROFILE) { imageUrl ->
                    imageUrl?.let {
                        user.image = it
                        ProgressDialog.getInstance(requireContext()).hide()
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
