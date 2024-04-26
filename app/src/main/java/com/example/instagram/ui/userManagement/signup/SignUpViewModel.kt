package com.example.instagram.ui.userManagement.signup

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagram.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SignUpViewModel : ViewModel() {

    private val mAuth = FirebaseAuth.getInstance()
    private val usersCollection = FirebaseFirestore.getInstance().collection(Constants.USER)

    private val _authenticationState = MutableLiveData<AuthenticationState>()
    val authenticationState: LiveData<AuthenticationState>
        get() = _authenticationState

    fun signUpWithEmailAndPassword(email: String, name: String, password: String, imageUri: Uri?) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val userId = user?.uid ?: ""

                    val userData = hashMapOf(
                        Constants.UID to userId,
                        Constants.EMAIL to email,
                        Constants.NAME to name,
                        Constants.PASSWORD to password
                    )

                    imageUri?.let { uri ->
                        FirebaseStorage.getInstance().getReference(Constants.USER_PROFILE)
                            .child(userId)
                            .putFile(uri)
                            .addOnSuccessListener { taskSnapshot ->
                                taskSnapshot.storage.downloadUrl.addOnSuccessListener { imageUrl ->
                                    userData[Constants.Image] = imageUrl.toString()

                                    usersCollection.document(userId)
                                        .set(userData)
                                        .addOnSuccessListener {
                                            _authenticationState.postValue(AuthenticationState.AUTHENTICATED)
                                        }
                                        .addOnFailureListener { e ->
                                            Log.d("AuthFail", e.message ?: "Unknown error")
                                            _authenticationState.postValue(AuthenticationState.UNAUTHENTICATED)
                                        }
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.d("ImageUploadFail", e.message ?: "Unknown error")
                                _authenticationState.postValue(AuthenticationState.UNAUTHENTICATED)
                            }
                    }
                } else {
                    Log.d("AuthenticationError", "Authentication failed", task.exception)
                    _authenticationState.postValue(AuthenticationState.UNAUTHENTICATED)
                }
            }
    }

    sealed class AuthenticationState {
        data object AUTHENTICATED : AuthenticationState()
        data object UNAUTHENTICATED : AuthenticationState()
    }


}