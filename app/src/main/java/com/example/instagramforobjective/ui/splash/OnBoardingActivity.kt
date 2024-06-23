package com.example.instagramforobjective.ui.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.base.BaseActivity
import com.example.instagramforobjective.ui.user.login.LoginActivity
import com.example.instagramforobjective.utils.Constants
import com.example.instagramforobjective.utils.helpers.PreferenceHelper
import com.example.instagramforobjective.R
import com.example.instagramforobjective.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : BaseActivity() {

    lateinit var binding: ActivityOnBoardingBinding
    private lateinit var sharedPreferences: SharedPreferences
    val pHelpers by lazy {

        PreferenceHelper(this)
    }
    private var currentPosition = 0
    private val imgList = arrayOf(
        R.drawable.reaction,
        R.drawable.post,
        R.drawable.stories,
        R.drawable.location,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE)

        initComponents()
    }


    override fun initComponents() {
        Log.d(javaClass.simpleName, "initComponents: OnBoardingActivity ")
        binding.nextButton.setOnClickListener {
            currentPosition++
            setImages()

            if (currentPosition == imgList.size) {
                pHelpers.setFirstTimeLaunch(sharedPreferences)
                startMainActivity()
            }
        }
        setImages()
        supportActionBar?.hide()
    }

    override fun defineLayout(): Int {
        return R.layout.activity_on_boarding
    }

    private fun startMainActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun postDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityOnBoardingBinding
    }

    private fun setImages() {
        if (currentPosition == imgList.size) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            binding.onboardingImgView.setImageDrawable(
                ContextCompat.getDrawable(
                    this@OnBoardingActivity,
                    imgList[currentPosition]
                )
            )
        }
        if (currentPosition == imgList.size - 1) {
            binding.nextButton.text = getString(R.string.finish)
            //binding.nextButton.icon = null
        }
    }
}