package com.example.instagramforobjective.ui.user.login

import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.ui.home.HomeActivity
import com.example.instagramforobjective.base.BaseActivity
import com.example.instagramforobjective.ui.user.signup.SignUpActivity
import com.example.instagramforobjective.utils.helpers.PreferenceHelper
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.ActivityLoginBinding


class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val pHelper by lazy {
        PreferenceHelper(this)
    }

    override fun initComponents() {
        Log.d(javaClass.simpleName, "initComponents: LoginActivity ")
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
        )
        val color: Int = getColor(R.color.sky)
        spannableString.setSpan(
            ForegroundColorSpan(color),
            spannableString.length - "Sign up".length,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.accountTextView.text = spannableString
        binding.accountTextView.movementMethod = LinkMovementMethod.getInstance()

        loginViewModel.loginInfo.observe(this) { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    pHelper.setLogin(true)
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    Log.d("TAG", "initComponents: Unauthorized")
                }
            }
        }

        binding.logInButton.setOnClickListener {
            val email = binding.usernameEText.text.toString()
            val password = binding.passwordEText.text.toString()
            loginViewModel.signInWithEmailAndPassword(email, password)
        }

        supportActionBar?.hide()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun defineLayout(): Int {
        return R.layout.activity_login
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityLoginBinding

    }

}