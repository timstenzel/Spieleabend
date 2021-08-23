package de.stenzel.tim.spieleabend.presentation.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    @ApplicationContext val context: Context
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn : LiveData<Boolean>
        get() = _isLoggedIn

    init {
        statusCheck()
    }

    fun statusCheck() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            _isLoggedIn.postValue(false)
        } else {
            _isLoggedIn.postValue(true)
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