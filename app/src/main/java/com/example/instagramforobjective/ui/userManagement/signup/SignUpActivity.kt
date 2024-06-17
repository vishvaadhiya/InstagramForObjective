package com.example.instagramforobjective.ui.userManagement.signup

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.base.BaseActivity
import com.example.instagramforobjective.ui.userManagement.login.LoginActivity
import com.example.instagramforobjective.data.models.User
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.utils.customViews.ProgressDialog
import com.example.instagramforobjective.utils.showToast
import com.example.instagramforobjective.utils.uploadImage
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : BaseActivity() {

    private var mAuth: FirebaseAuth? = null
    lateinit var binding: ActivitySignUpBinding
    private lateinit var user: User
    private var selectedImageUri: Uri? = null
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun initComponents() {
        Log.d(javaClass.simpleName, "initComponents: SignUpActivity ")
        user = User()
        mAuth = FirebaseAuth.getInstance()
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                ProgressDialog.showDialog(this as AppCompatActivity)
//                ProgressDialog.showDialog(this)
                uploadImage(this,uri, Constants.USER_PROFILE) {
                    selectedImageUri = uri
                    if (it != null) {
                        ProgressDialog.hideDialog()
//                        ProgressDialog.hideDialog()
                        user.image = it
                        binding.userProfileView.setImageURI(uri)
                    }
                }
            }
        }

        binding.userProfileView.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.AddProfileTv.setOnClickListener {
            pickImage.launch("image/*")
        }


        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val name = binding.usernameEditText.text.toString()
            signUpWithEmailAndPassword(email, name, password, selectedImageUri)
        }
        supportActionBar?.hide()
    }

    override fun defineLayout(): Int {
        return R.layout.activity_sign_up
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivitySignUpBinding
    }

    private fun signUpWithEmailAndPassword(
        email: String,
        name: String,
        password: String,
        imageUri: Uri?,
    ) {
        signUpViewModel.signUpWithEmailAndPassword(email, name, password, imageUri)
        signUpViewModel.authenticationState.observe(this) { authenticationstate->
            when(authenticationstate){
                SignUpViewModel.AuthenticationState.AUTHENTICATED ->{
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                SignUpViewModel.AuthenticationState.UNAUTHENTICATED ->{
                    showToast(getString(R.string.error_adding_user_data))
                }
                else -> {}
            }
        }
    }
}