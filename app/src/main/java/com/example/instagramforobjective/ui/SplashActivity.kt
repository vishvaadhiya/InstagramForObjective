package com.example.instagramforobjective.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.instagramforobjective.R
import com.example.instagramforobjective.ui.login.LoginActivity
import com.example.instagramforobjective.ui.onborading.OnBoardingActivity
import com.example.instagramforobjective.utility.Constants
import com.example.instagramforobjective.utility.PreferenceHelper
import com.example.instagramforobjective.utility.goToMainActivity

class SplashActivity : AppCompatActivity() {

    lateinit var context: Context

    var TAG = SplashActivity::class.java.simpleName
    private lateinit var sharedPreferences: SharedPreferences
    val pHelpers by lazy {

        PreferenceHelper(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this@SplashActivity
        sharedPreferences = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE)
        loginCheck()
    }

    fun gotoLogin() {

        val iLogin = Intent(this, LoginActivity::class.java)
        startActivity(iLogin)
    }

    fun gotoOnBorading() {

        val onBoarding = Intent(this, OnBoardingActivity::class.java)
        startActivity(onBoarding)
    }


    fun loginCheck() {
        val h: Handler = Handler()
        h.postDelayed(Runnable {


            if (pHelpers.isLogin() == true) {
                goToMainActivity()
            }else if(pHelpers.isFirstTimeLaunch(sharedPreferences)){
                gotoOnBorading()
            } else {
                gotoLogin()
            }


            finish()

        }, 2000)

    }
}