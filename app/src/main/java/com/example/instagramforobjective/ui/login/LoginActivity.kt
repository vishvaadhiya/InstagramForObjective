package com.example.instagramforobjective.ui.login

import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.MainActivity
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivityLoginBinding
import com.example.instagramforobjective.ui.signup.SignUpActivity
import com.example.instagramforobjective.utility.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import java.lang.Exception

class LoginActivity : BaseActivity() {

    private var mAuth: FirebaseAuth? = null
    private lateinit var binding: ActivityLoginBinding

    override fun initComponents() {
        val spannableString = SpannableString(getString(R.string.don_t_have_an_account))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                startActivity(Intent(applicationContext, SignUpActivity::class.java))
            }
        }

        spannableString.setSpan(
            clickableSpan,
            spannableString.length - "Sign up".length,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        val color: Int = getColor(R.color.sky)
        spannableString.setSpan(
            ForegroundColorSpan(color),
            spannableString.length - "Sign up".length,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.accountTextView.text = spannableString
        binding.accountTextView.movementMethod = LinkMovementMethod.getInstance()

        mAuth = FirebaseAuth.getInstance()

        binding.logInButton.setOnClickListener {
            val email = binding.usernameEText.text.toString()
            val password = binding.passwordEText.text.toString()
            try {
                mAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(this) {
                        showToast(getString(R.string.login_successful))
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener(this) { e ->
                        when (e) {
                            is FirebaseAuthInvalidUserException -> {
                                showToast(getString(R.string.login_invalid_user))
                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                showToast(getString(R.string.login_credential_is_invalid))
                            }

                            else -> {
                                Log.d("tag", "Other exception: $e")
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.d("tag", "Other exception: $e")
            }
        }

        supportActionBar?.hide()
    }

    override fun defineLayout(): Int {
        return R.layout.activity_login
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityLoginBinding

    }

}