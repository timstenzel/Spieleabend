package de.stenzel.tim.spieleabend.presentation.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
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

    fun saveNotificationSubscriptions(topicList: List<String>, booleanList: List<Boolean>) {

        val subTo = arrayListOf<String>()
        val unsubFrom = arrayListOf<String>()

        for (topic in booleanList.withIndex()) {
            if (booleanList[topic.index]) {
                subTo.add(topicList[topic.index])
            } else {
                unsubFrom.add(topicList[topic.index])
            }
        }

        subscribeDeviceToTopics(subTo)
        unsubscribeDeviceFromTopics(unsubFrom)
    }

    private fun subscribeDeviceToTopics(topics: List<String>) {

        for (topic in topics) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        Log.d("Profile VM", "sub to $topic: success")
                    } else {
                        Log.d("Profile VM", "sub to $topic: fail")
                    }
                }
        }
    }

    private fun unsubscribeDeviceFromTopics(topics: List<String>) {

        for (topic in topics) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        Log.d("Profile VM", "unsub from $topic: success")
                    } else {
                        Log.d("Profile VM", "unsub from $topic: fail")
                    }
                }

        }
    }
}