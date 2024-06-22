package com.example.instagramforobjective.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.instagramforobjective.ui.user.login.LoginActivity
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.utils.helpers.PreferenceHelper
import com.example.instagramforobjective.utils.goToMainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    lateinit var context: Context

    var TAG = SplashActivity::class.java.simpleName
    private lateinit var sharedPreferences: SharedPreferences
    private val pHelpers by lazy {

        PreferenceHelper(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this@SplashActivity
        sharedPreferences = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE)
        loginCheck()
    }

    private fun gotoLogin() {
        val iLogin = Intent(this, LoginActivity::class.java)
        startActivity(iLogin)
    }

    private fun gotoOnBorading() {

        val onBoarding = Intent(this, OnBoardingActivity::class.java)
        startActivity(onBoarding)
    }


    private fun loginCheck() {
        val h = Handler()
        h.postDelayed({
            if (pHelpers.isLogin() == true) {
                goToMainActivity()
            } else if (pHelpers.isFirstTimeLaunch(sharedPreferences)) {
                gotoOnBorading()
            } else {
                gotoLogin()
            }
            finish()

        }, 2000)

    }
}