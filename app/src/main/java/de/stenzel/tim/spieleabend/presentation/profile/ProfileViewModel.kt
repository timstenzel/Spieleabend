package de.stenzel.tim.spieleabend.presentation.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser>()
    val user : LiveData<FirebaseUser>
        get() = _user


    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn : LiveData<Boolean>
        get() = _isLoggedIn

    init {
        statusCheck()
    }

    fun statusCheck() {
        when (val user = FirebaseAuth.getInstance().currentUser) {
            null -> {
                _isLoggedIn.value = false
            }
            else -> {
                _isLoggedIn.value = true
                _user.value = user!!
            }
        }
    }

    fun logoutUser() {
        AuthUI.getInstance()
            .signOut(context)
            .addOnCompleteListener {
                _isLoggedIn.postValue(false)
            }
    }
}