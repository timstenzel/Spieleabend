package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.helpers.*
import de.stenzel.tim.spieleabend.models.local.*
import de.stenzel.tim.spieleabend.repositories.DominionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DominionViewModel @Inject constructor(
    private val repository: DominionRepository
) : ViewModel() {

    private val _preparedDominionData = MutableLiveData<ArrayList<Any>>()
    val preparedDominionData : LiveData<ArrayList<Any>>
        get() = _preparedDominionData

    private val _generatedDeck = MutableLiveData<Event<Resource<DominionDeck>>>()
    val generatedDeck : LiveData<Event<Resource<DominionDeck>>>
        get() = _generatedDeck

    init {
        prepopulateDB()
        prepareData()
    }

    private fun prepopulateDB() {

        viewModelScope.launch {
            repository.insertExpansions(expansionsForPrepopulation())
            repository.insertCards(cardsForPrepopulation())
            repository.insertEvents(eventsForPrepopulation())
            repository.insertLandmarks(landmarksForPrepopulation())
            repository.insertPriceCurves(priceCurvesForPrepopulation())
        }

    }

    private fun prepareData() {

        viewModelScope.launch {
            val expansions = repository.getExpansions()
            val cards = repository.getCards()

            val preparedList = arrayListOf<Any>()

            for (e in expansions) {
                preparedList.add(e)
                for (c in cards) {
                    if (c.expansionId == e.id) {
                        preparedList.add(c)
                    }
                }
            }
            _preparedDominionData.postValue(preparedList)
        }
    }

    fun generateDeck(selectedCards : ArrayList<Int>) {

        _generatedDeck.value = Event(Resource.loading(null))

        viewModelScope.launch {

            val cards = repository.getCards()
            val priceCurves = repository.getPriceCurves()
            val events = repository.getEvents()
            val landmarks = repository.getLandmarks()
            
            //price <= 2
            val cardsCost2 = mutableListOf<DominionCard>()
            val cardsCost3 = mutableListOf<DominionCard>()
            val cardsCost4 = mutableListOf<DominionCard>()
            val cardsCost5 = mutableListOf<DominionCard>()
            //price >= 6
            val cardsCost6 = mutableListOf<DominionCard>()

            for (card in cards) {
                if (selectedCards.contains(card.id)) {
                    //card was selected -> add to list
                    when {
                        card.price <= 2 -> {
                            cardsCost2.add(card)
                        }
                        card.price == 3 -> {
                            cardsCost3.add(card)
                        }
                        card.price == 4 -> {
                            cardsCost4.add(card)
                        }
                        card.price == 5 -> {
                            cardsCost5.add(card)
                        }
                        else -> {
                            cardsCost6.add(card)
                        }
                    }
                }
            }

            val curve = getRandomPriceCurve(priceCurves)

            val cost2Indices = mutableListOf<Int>()
            val cost3Indices = mutableListOf<Int>()
            val cost4Indices = mutableListOf<Int>()
            val cost5Indices = mutableListOf<Int>()
            val cost6Indices = mutableListOf<Int>()

            while (cost2Indices.size < curve.twoOrBelow) {
                val r = Random.nextInt(0, cardsCost2.size)
                if (!cost2Indices.contains(r)) {
                    cost2Indices.add(r)
                }
            }
            while (cost3Indices.size < curve.three) {
                val r = Random.nextInt(0, cardsCost3.size)
                if (!cost3Indices.contains(r)) {
                    cost3Indices.add(r)
                }
            }
            while (cost4Indices.size < curve.four) {
                val r = Random.nextInt(0, cardsCost4.size)
                if (!cost4Indices.contains(r)) {
                    cost4Indices.add(r)
                }
            }
            while (cost5Indices.size < curve.five) {
                val r = Random.nextInt(0, cardsCost5.size)
                if (!cost5Indices.contains(r)) {
                    cost5Indices.add(r)
                }
            }
            while (cost6Indices.size < curve.sixOrAbove) {
                val r = Random.nextInt(0, cardsCost6.size)
                if (!cost6Indices.contains(r)) {
                    cost6Indices.add(r)
                }
            }

            val chosenCards = mutableListOf<DominionCard>()
            for (i in cost2Indices) {
                chosenCards.add(cardsCost2[i])
            }
            for (i in cost3Indices) {
                chosenCards.add(cardsCost3[i])
            }
            for (i in cost4Indices) {
                chosenCards.add(cardsCost4[i])
            }
            for (i in cost5Indices) {
                chosenCards.add(cardsCost5[i])
            }
            for (i in cost6Indices) {
                chosenCards.add(cardsCost6[i])
            }

            val chosenEvent = getRandomEvent(events)
            val chosenLandmark = getRandomLandmark(landmarks)

            val generatedDeck = DominionDeck(chosenCards, chosenEvent, chosenLandmark)

            _generatedDeck.value = Event(Resource.success(generatedDeck))

        }
    }


    private fun getRandomPriceCurve(curves: List<DominionPriceCurve>) : DominionPriceCurve {
        val r = Random.nextInt(0, curves.size)
        return curves[r]
    }

    private fun getRandomEvent(events: List<DominionEvent>) : DominionEvent {
        val r = Random.nextInt(0, events.size)
        return events[r]
    }

    private fun getRandomLandmark(landmarks: List<DominionLandmark>) : DominionLandmark {
        val r = Random.nextInt(0, landmarks.size)
        return landmarks[r]
    }

}




















