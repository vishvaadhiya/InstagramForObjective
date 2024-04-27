package com.example.instagram.ui.userManagement.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class LoginViewModel : ViewModel() {

    private val mAuth = FirebaseAuth.getInstance()

    private val _loginInfo = MutableLiveData<AuthenticationState>()
    val loginInfo: LiveData<AuthenticationState>
        get() = _loginInfo


    fun signInWithEmailAndPassword(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                _loginInfo.postValue(AuthenticationState.AUTHENTICATED)
            }
            .addOnFailureListener { e ->
                when (e) {
                    is FirebaseAuthInvalidUserException -> {
                        _loginInfo.postValue(AuthenticationState.UNAUTHENTICATED)
                    }

                    is FirebaseAuthInvalidCredentialsException -> {
                        _loginInfo.postValue(AuthenticationState.UNAUTHENTICATED)
                    }

                    else -> {
                        Log.d("AuthenticationError", "Other exception: $e")
                    }
                }
                _loginInfo.postValue(AuthenticationState.UNAUTHENTICATED)
            }
    }

    sealed class AuthenticationState {
        data object AUTHENTICATED : AuthenticationState()
        data object UNAUTHENTICATED : AuthenticationState()
    }
}