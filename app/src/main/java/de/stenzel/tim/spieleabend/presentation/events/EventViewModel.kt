package de.stenzel.tim.spieleabend.presentation.events

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.helpers.isNetworkAvailable
import de.stenzel.tim.spieleabend.helpers.timestampToLocalDate
import de.stenzel.tim.spieleabend.models.EventHeader
import de.stenzel.tim.spieleabend.models.EventModel
import de.stenzel.tim.spieleabend.models.NewsModel
import java.io.IOException
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class EventViewModel @Inject constructor(
    private val application: Application,
    private val db : FirebaseDatabase
) : ViewModel() {

    private val _events = MutableLiveData<Resource<List<Any>>>()
    val events : LiveData<Resource<List<Any>>>
        get() = _events
    
    init {
        getAllEvents()
    }

    fun getAllEvents() {

        try {
            if (isNetworkAvailable(application)) {
                _events.postValue(Resource.Loading())

                val listener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        _events.postValue(handleNewsResponse(snapshot))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _events.postValue(Resource.Error(error.message))
                    }
                }
                db.getReference("events_list").addValueEventListener(listener)
            } else {
                _events.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _events.postValue(Resource.Error("Network failure"))
                }
                else -> {
                    _events.postValue(Resource.Error("Conversion error"))
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

        return Resource.Success(finalList)
    }

    private fun prepareData(eventList : ArrayList<EventModel>) : ArrayList<Any> {
        //iterate over list and insert headers
        val finalList = arrayListOf<Any>()

        eventList.sortBy { it.startDate }

        for (e in eventList) {
            val date = timestampToLocalDate(e.startDate)

            val header = EventHeader("${date.month.getDisplayName(TextStyle.FULL, Locale.GERMAN)} ${date.year}")

            if (!finalList.contains(header)) {
                finalList.add(header)
            }
            finalList.add(e)
        }

        return finalList
    }
}