package com.example.instagramforobjective.ui.dashboard

import android.app.ProgressDialog
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseFragment
import com.example.instagramforobjective.databinding.FragmentUpdateProfileBinding
import com.example.instagramforobjective.ui.model.UserModel
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.showToast
import com.example.instagramforobjective.utility.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class UpdateProfileFragment : BaseFragment() {

    lateinit var binding: FragmentUpdateProfileBinding
    lateinit var user: UserModel
    private lateinit var progressDialog: ProgressDialog

    override fun defineLayout(): Int {
        return R.layout.fragment_update_profile
    }

    override fun postDataBinding(binding: ViewDataBinding): ViewDataBinding {
        this.binding = binding as FragmentUpdateProfileBinding
        return this.binding
    }

    override fun initComponent() {
        user = UserModel()
        progressDialog = ProgressDialog(activity)
        Firebase.firestore.collection(Constants.USER)
            .document(FirebaseAuth.getInstance().currentUser!!.uid).get()
            .addOnSuccessListener {
                user = it.toObject<UserModel>()!!

                if (!user.image.isNullOrEmpty()) {
                    Glide.with(requireActivity())
                        .load(user.image)
                        .placeholder(R.drawable.user)
                        .into(binding.userProfileView)
                }
                binding.updateEmailEditText.setText(user.email)
                binding.updateUsernameEditText.setText(user.name)
                binding.updatePasswordEditText.setText(user.password)
            }
        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                com.example.instagramforobjective.utility.ProgressDialog.showDialog(activity as AppCompatActivity)
                uploadImage(uri, Constants.USER_PROFILE) {
                    if (it != null) {
                        user.image = it
                        com.example.instagramforobjective.utility.ProgressDialog.hideDialog()
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

            user.apply {
                email = newEmail
                name = newName
                password = newPassword
            }

            Firebase.firestore.collection(Constants.USER)
                .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .set(user)
                .addOnSuccessListener {
                    activity?.showToast("Successfully Updated")
                    startActivity(Intent(activity, MainActivity::class.java))
                }
        }
    }


}