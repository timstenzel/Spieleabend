package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LuckyWheelViewModel @Inject constructor(

) : ViewModel() {

    private val _luckyWinner = MutableLiveData<Int>()
    val luckyWinner : LiveData<Int>
        get() = _luckyWinner


    fun startSpinning(amount: Int) {
        val r = Random.nextInt(1, amount + 1)
        _luckyWinner.postValue(r)
    }

}