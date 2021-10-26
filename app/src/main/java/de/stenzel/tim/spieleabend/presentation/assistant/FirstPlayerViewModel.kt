package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.helpers.Event
import de.stenzel.tim.spieleabend.helpers.Resource
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FirstPlayerViewModel @Inject constructor() : ViewModel() {

    private val _amount = MutableLiveData<Event<Resource<Int>>>()
    val amount : LiveData<Event<Resource<Int>>>
        get() = _amount

    fun validateInput(input: String) {
        try {
            val amount = input.toInt()
            when {
                amount < 2 -> {
                    _amount.postValue(Event(Resource.error("Amount < 2", null)))
                }
                amount > 8 -> {
                    _amount.postValue(Event(Resource.error("Amount > 8", null)))
                }
                else -> {
                    _amount.postValue(Event(Resource.success(amount)))
                }
            }
        } catch (e: Exception) {
            _amount.postValue(Event(Resource.error("Input is not a number", null)))
        }


    }

}