package com.example.instagramforobjective.ui.onborading

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.example.instagramforobjective.R
import com.example.instagramforobjective.common.BaseActivity
import com.example.instagramforobjective.databinding.ActivityOnBoardingBinding
import com.example.instagramforobjective.ui.login.LoginActivity
import com.example.instagramforobjective.utility.Constants

class OnBoardingActivity : BaseActivity() {

    lateinit var binding: ActivityOnBoardingBinding
    private lateinit var sharedPreferences: SharedPreferences

    private var currentPosition = 0
    private val imgList = arrayOf(
        R.drawable.reaction,
        R.drawable.post,
        R.drawable.location,
        R.drawable.stories
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE)

        if (isFirstTimeLaunch(sharedPreferences)) {
            initComponents()
        } else {
            startMainActivity()
        }
    }

    private fun isFirstTimeLaunch(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(Constants.ISFIRSTTIMELAUNCH, true)
    }

    private fun setFirstTimeLaunch(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().putBoolean(Constants.ISFIRSTTIMELAUNCH, false).apply()
    }

    override fun initComponents() {
        binding.nextButton.setOnClickListener {
            currentPosition++
            setImages()

            if (currentPosition == imgList.size) {
                setFirstTimeLaunch(sharedPreferences)
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
            binding.nextButton.icon = null
        }
    }
}