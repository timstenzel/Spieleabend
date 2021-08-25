package de.stenzel.tim.spieleabend.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.models.NewsModel
import de.stenzel.tim.spieleabend.models.NotificationTokenModel
import de.stenzel.tim.spieleabend.presentation.MainActivity
import javax.inject.Inject

/**
 * Service handling incoming messages from firebase cloud messaging
 * Code from: https://github.com/firebase/quickstart-android (23.8.21)
 */
@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var db : FirebaseDatabase

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        /*
         There are two types of messages data messages and notification messages. Data messages are handled
         here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
         traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
         is in the foreground. When the app is in the background an automatically generated notification is displayed.
         When the user taps on the notification they are returned to the app. Messages containing both notification
         and data payloads are treated as notification messages. The Firebase console always sends notification
         messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        */

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            //data messages are not handled since the firebase console only sends notifications
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let { payload ->
            Log.d(TAG, "Message Notification Body: ${payload.body}")

            //this is only called when the app is in foreground
            if (payload.title != null && payload.body != null) {
                showNotification(payload.title!!, payload.body!!)
            }
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param title FCM message title
     * @param messageBody FCM message body received.
     */
    private fun showNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(channelId, getString(R.string.default_notification_channel_name), NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "new token received: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {

                task.result?.let { id ->
                    sendRegistrationToServer(id, token, System.currentTimeMillis()/1000)
                }
            }
        }
    }


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(id: String, token: String, timestamp: Long) {

        //make request
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<NotificationTokenModel>()
                val keyList = arrayListOf<String>()
                for (sn : DataSnapshot in snapshot.children) {
                    val key = sn.key
                    keyList.add(key!!)
                    val model = sn.getValue(NotificationTokenModel::class.java)
                    list.add(model!!)
                }

                var exists = false
                for ((index, ntm) in list.withIndex()) {
                    //check if id already exists
                    if (id == ntm.installationId) {
                        exists = true
                        //token is also equal so dont do anything
                        if (token == ntm.token) {
                            //no update needed
                            Log.d("MyFirebaseMService", "Id and Token already there")
                        } else {
                            //id exists but with an old token
                            updateToken(keyList[index], id, token, timestamp)
                        }
                    }
                }
                if (!exists) {
                    //create new entry for this id and token
                    val lastKey = keyList.last()
                    val lastKeyInt = lastKey.toInt()
                    saveIdTokenPair((lastKeyInt+1).toString(), id, token, timestamp)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "retrieval from db failed")
            }
        }

        db.getReference("fcm_tokens").addValueEventListener(listener)
    }


    private fun updateToken(key: String, id: String, token: String, timestamp: Long) {
        Log.d(TAG, "Id there, update token")
        val model = NotificationTokenModel(id, token, timestamp)
        db.getReference("fcm_tokens").child(key).setValue(model)
    }

    private fun saveIdTokenPair(key: String, id: String, token: String, timestamp: Long) {
        Log.d(TAG, "Save id and token")
        val model = NotificationTokenModel(id, token, timestamp)
        db.getReference("fcm_tokens").child(key).setValue(model)
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}