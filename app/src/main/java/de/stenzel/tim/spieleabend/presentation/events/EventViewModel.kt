package de.stenzel.tim.spieleabend.presentation.events

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.helpers.timestampToLocalDate
import de.stenzel.tim.spieleabend.models.EventHeader
import de.stenzel.tim.spieleabend.models.EventModel
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class EventViewModel
@Inject
constructor(private val db : FirebaseDatabase) : ViewModel() {

    private val _events = MutableLiveData<ArrayList<Any>>()
    val events : LiveData<ArrayList<Any>>
        get() = _events


    init {
        getAllEvents()
    }

    private fun getAllEvents() {

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<EventModel>()
                for (sn : DataSnapshot in snapshot.children) {
                    val event = sn.getValue(EventModel::class.java)
                    list.add(event!!)
                }

                val finalList = prepareData(list)

                _events.postValue(finalList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("EventVM", "retrieval from db failed")
            }
        }

        db.getReference("events_list").addValueEventListener(listener)
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