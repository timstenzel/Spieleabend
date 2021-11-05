package de.stenzel.tim.spieleabend.presentation.events

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.helpers.showToast
import de.stenzel.tim.spieleabend.helpers.timestampToLocalDate
import de.stenzel.tim.spieleabend.models.local.LocationModel
import de.stenzel.tim.spieleabend.models.remote.EventHeader
import de.stenzel.tim.spieleabend.models.remote.EventModel
import java.io.IOException
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val db : FirebaseDatabase
) : ViewModel() {

    private val _events = MutableLiveData<Resource<List<Any>>>()
    val events : LiveData<Resource<List<Any>>>
        get() = _events

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn : LiveData<Boolean>
        get() = _isLoggedIn

    private val _location = MutableLiveData<Resource<LocationModel>>()
    val location : LiveData<Resource<LocationModel>>
        get() = _location


    fun getAllEvents() {
        try {
            _events.postValue(Resource.loading(null))

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _events.postValue(handleNewsResponse(snapshot))
                }

                override fun onCancelled(error: DatabaseError) {
                    _events.postValue(Resource.error(error.message, null))
                }
            }
            db.getReference("events_list").addValueEventListener(listener)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _events.postValue(Resource.error("Network failure", null))
                }
                else -> {
                    _events.postValue(Resource.error("Conversion error", null))
                }
            }
        }
    }

    private fun handleNewsResponse(snapshot: DataSnapshot) : Resource<List<Any>> {

        val list = arrayListOf<EventModel>()
        for (sn : DataSnapshot in snapshot.children) {
            val event = sn.getValue(EventModel::class.java)
            list.add(event!!)
        }

        val finalList = prepareData(list)

        return Resource.success(finalList)
    }

    private fun prepareData(eventList : ArrayList<EventModel>) : ArrayList<Any> {
        //iterate over list and insert headers
        val finalList = arrayListOf<Any>()

        eventList.sortBy { it.startDate }

        for (e in eventList) {
            val date = timestampToLocalDate(e.startDate!!)

            val header = EventHeader("${date.month.getDisplayName(TextStyle.FULL, Locale.GERMAN)} ${date.year}")

            if (!finalList.contains(header)) {
                finalList.add(header)
            }
            finalList.add(e)
        }

        return finalList
    }

    fun getCurrentLocation() {
        try {
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token)
                .addOnSuccessListener { location ->
                    val currLocationLat = location.latitude
                    val currLocationLon = location.longitude
                    _location.postValue(Resource.success(LocationModel(currLocationLat, currLocationLon)))
                }
                .addOnFailureListener {
                    _location.postValue(Resource.error("Standort nicht gefunden", null))
                }
        } catch (e : SecurityException) {
            _location.postValue(Resource.error("Keine Berechtigung für Standortabfragen", null))
        }

    }

    fun statusCheck() {
        val user = FirebaseAuth.getInstance().currentUser
        _isLoggedIn.value = when (user) {
            null -> false
            else -> true
        }
    }
}