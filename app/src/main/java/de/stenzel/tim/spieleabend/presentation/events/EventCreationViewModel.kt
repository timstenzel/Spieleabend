package de.stenzel.tim.spieleabend.presentation.events

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.helpers.Event
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.models.remote.EventModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EventCreationViewModel @Inject constructor(
    private val storage: FirebaseStorage,
    private val db: FirebaseDatabase
) : ViewModel() {

    private val _formErrors = MutableLiveData<List<FormErrors>>()
    val formErrors : LiveData<List<FormErrors>>
        get() = _formErrors

    private val _state = MutableLiveData<Event<Resource<String>>>()
    val state : LiveData<Event<Resource<String>>>
        get() = _state

    fun createEvent(imageUri: Uri?, title: String, description: String, location: String,
                    latitude: String, longitude: String, now: Calendar, startDate: String,
                    startTime: String, start: Calendar, endDate: String, endTime: String,
                    end: Calendar, public: Boolean) {

        val imageIdTitle = title.lowercase().replace(" ", "_")
        val imageIdDate = startDate.replace(".", "_")
        val imageId = "${imageIdTitle}_${imageIdDate}.jpg"
        val completeImageId = if (imageUri != null) {
            "${Constants.URL_FIRE_STORAGE}${Constants.URL_FIRE_STORAGE_EVENTS}$imageId"
        } else {
            "${Constants.URL_FIRE_STORAGE}${Constants.URL_FIRE_STORAGE_EVENTS}${Constants.FILE_NAME_EVENT_DEFAULT}"
        }

        //get userId and display name from firebase auth
        val pair = statusCheck()
        val userId = pair.first
        val displayName = pair.second

        //prepare start date
        val startSeconds = start.timeInMillis / 1000

        //prepare end date
        val endSeconds = end.timeInMillis / 1000

        val event = EventModel(completeImageId, title, description, public, startSeconds, endSeconds, userId, displayName, location, latitude, longitude)

        //upload image to firebase storage
        if (imageUri != null) {
            val storageRef = storage.reference
            //val imageRef = storageRef.child(imageId)
            val eventImageRef = storageRef.child("${Constants.URL_FIRE_STORAGE_EVENTS}/$imageId")

            val uploadTask = eventImageRef.putFile(imageUri)
            uploadTask.addOnFailureListener {
                _state.value = Event(Resource.error("Image could not be uploaded", null))
            }.addOnSuccessListener {
                //upload event to realtime db
                uploadEventToDB(event)
            }
        } else {
            //use default image from constant link
            uploadEventToDB(event)
        }
    }

    private fun uploadEventToDB(event: EventModel) {

        db.getReference("events_list").child(UUID.randomUUID().toString()).setValue(event)
            .addOnFailureListener {
                _state.value = Event(Resource.error("Event could not be uploaded", null))
            }.addOnSuccessListener {
                _state.value = Event(Resource.success(null))
            }
    }

    fun isFormValid(title: String, description: String, location: String, latitude: String,
                    longitude: String, now: Calendar, startDate: String, startTime: String,
                    start: Calendar, endDate: String, endTime: String, end: Calendar
                    ) : Boolean{
        val errorList = mutableListOf<FormErrors>()

        if (title.isEmpty()) {
            errorList.add(FormErrors.MISSING_TITLE)
        }
        if (description.isEmpty()) {
            errorList.add(FormErrors.MISSING_DESCRIPTION)
        }
        if (location.isEmpty()) {
            errorList.add(FormErrors.MISSING_LOCATION)
        }
        if (latitude.isEmpty()) {
            errorList.add(FormErrors.MISSING_LAT)
        }
        if (longitude.isEmpty()) {
            errorList.add(FormErrors.MISSING_LON)
        }
        if (startDate.isEmpty()) {
            errorList.add(FormErrors.MISSING_START_DATE)
        }
        if (startTime.isEmpty()) {
            errorList.add(FormErrors.MISSING_START_TIME)
        }
        if (endDate.isEmpty()) {
            errorList.add(FormErrors.MISSING_END_DATE)
        }
        if (endTime.isEmpty()) {
            errorList.add(FormErrors.MISSING_END_TIME)
        }
        if (start.timeInMillis < now.timeInMillis) {
            errorList.add(FormErrors.START_IN_PAST)
        }
        if (end.timeInMillis < now.timeInMillis) {
            errorList.add(FormErrors.END_IN_PAST)
        }
        if (start.timeInMillis >= end.timeInMillis) {
            errorList.add(FormErrors.END_BEFORE_START)
        }
        _formErrors.value = errorList

        return errorList.isEmpty()
    }

    private fun statusCheck(): Pair<String, String> {
        val user = FirebaseAuth.getInstance().currentUser
        val isLoggedIn = when (user) {
            null -> false
            else -> true
        }

        return if (isLoggedIn) {
            val id = user!!.uid
            val name = user.displayName!!
            Pair(id, name)
        } else {
            // TODO: 20.10.21 handle error
            Pair("", "")
        }
    }

    enum class FormErrors {
        MISSING_TITLE,
        MISSING_DESCRIPTION,
        MISSING_LOCATION,
        MISSING_LAT,
        MISSING_LON,
        MISSING_START_DATE,
        MISSING_START_TIME,
        MISSING_END_DATE,
        MISSING_END_TIME,
        START_IN_PAST,
        END_IN_PAST,
        END_BEFORE_START
    }

}