package com.example.instagramforobjective.ui.signup

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivitySignUpBinding
import com.example.instagramforobjective.ui.login.LoginActivity
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URL

class SignUpActivity : BaseActivity() {

    private var mAuth: FirebaseAuth? = null
    lateinit var binding: ActivitySignUpBinding
    private var selectedImageUri: Uri? = null

    override fun initComponents() {
        mAuth = FirebaseAuth.getInstance()
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                binding.userProfileView.setImageURI(uri)
            }
        }

        binding.userProfileView.setOnClickListener {
            pickImage.launch("image/*")
        }


        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val name = binding.usernameEditText.text.toString()
            signUpWithEmailAndPassword(email, name, password,selectedImageUri)
        }
        supportActionBar?.hide()
    }

    override fun defineLayout(): Int {
        return R.layout.activity_sign_up
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivitySignUpBinding
    }

    private fun signUpWithEmailAndPassword(email: String, name: String, password: String,imageUri:Uri?) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val userId = user?.uid ?: ""

                    val usersCollection = FirebaseFirestore.getInstance().collection(Constants.USER)
                    val userDocument = usersCollection.document(userId)
                    val userData = hashMapOf(
                        Constants.EMAIL to email,
                        Constants.NAME to name,
                        Constants.PASSWORD to password,
                        Constants.Image to imageUri.toString()

                    )

                    userDocument.set(userData)
                        .addOnSuccessListener {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                            Log.d("Auth", task.exception.toString())
                        }
                        .addOnFailureListener { e ->
                            showToast(getString(R.string.error_adding_user_data))
                            Log.d("AuthFail", task.exception.toString())
                        }
                    showToast(getString(R.string.authentication_success))
                } else {
                    Log.d("AuthenticationError", "Authentication failed", task.exception)
                    showToast(getString(R.string.authentication_failed))
                }
            }
    }
}