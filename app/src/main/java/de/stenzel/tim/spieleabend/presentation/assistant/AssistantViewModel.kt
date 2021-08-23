package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.models.AssistantModel

class AssistantViewModel : ViewModel() {

    private val _items = MutableLiveData<ArrayList<AssistantModel>>()
    val items : LiveData<ArrayList<AssistantModel>>
        get() = _items

    init {
        getAllItems()
    }

    private fun getAllItems() {

        val list = arrayListOf<AssistantModel>()

        //item 1: dominion deck generator
        val dominionGen = AssistantModel(R.drawable.news_default, "Dominion Deck Generator")
        list.add(dominionGen)

        //item 2: determine who begins
        val beginner = AssistantModel(R.drawable.news_default, "Wer beginnt?")
        list.add(beginner)

        //add more here...


        _items.postValue(list)
    }

}