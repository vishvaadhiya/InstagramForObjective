package com.example.instagramforobjective

import android.app.Application
import com.google.firebase.FirebaseApp

/*@HiltAndroidApp*/
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}