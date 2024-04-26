package com.example.instagram.utils.helpers

import android.content.Context
import android.content.SharedPreferences
import com.example.instagram.utils.Constants

class PreferenceHelper(context: Context) {
    private var sharedPreferences: SharedPreferences? = null
    private val PREFS_FILENAME = "global pref"
    private val KEY_IS_LOGIN = "is_login"

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    }

    fun isFirstTimeLaunch(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(Constants.IS_FIRST_TIME_LAUNCH, true)
    }

    fun setFirstTimeLaunch(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().putBoolean(Constants.IS_FIRST_TIME_LAUNCH, false).apply()
    }

    fun saveLikeState(postId: String, userId: String, isLiked: Boolean) {
        val key = generateLikeKey(postId, userId)
        sharedPreferences?.edit()?.putBoolean(key, isLiked)?.apply()
    }

    fun loadLikeState(postId: String, userId: String): Boolean {
        val key = generateLikeKey(postId, userId)
        return sharedPreferences?.getBoolean(key, false) == true
    }


    private fun generateLikeKey(postId: String, userId: String): String {
        return "like_${postId}_$userId"
    }

    fun setLogin(flag: Boolean) {
        sharedPreferences?.edit()?.putBoolean(KEY_IS_LOGIN, flag)?.apply()
    }

    fun isLogin(): Boolean? {
        return sharedPreferences?.getBoolean(KEY_IS_LOGIN, false)
    }

    fun clearPref() {

        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
    }
}


