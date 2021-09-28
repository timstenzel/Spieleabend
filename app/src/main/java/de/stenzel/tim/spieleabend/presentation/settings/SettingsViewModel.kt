package de.stenzel.tim.spieleabend.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging

class SettingsViewModel : ViewModel() {

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