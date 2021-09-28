package de.stenzel.tim.spieleabend.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class SubscriptionHelperService
@Inject
constructor(private val fireInstance : FirebaseMessaging) {

    fun subscribeToTopic(topic: String) {
        //Log.d("SubHelper", "sub to: $topic")
        fireInstance.subscribeToTopic(topic).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                Log.d("SubHelper", "sub to $topic: success")
            } else {
                Log.d("SubHelper", "sub to $topic: fail")
            }
        }
    }

    fun unsubscribeFromTopic(topic: String) {
        //Log.d("SubHelper", "unsub from: $topic")
        fireInstance.unsubscribeFromTopic(topic)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Log.d("SubHelper", "unsub from $topic: success")
                } else {
                    Log.d("SubHelper", "unsub from $topic: fail")
                }
            }
    }

}