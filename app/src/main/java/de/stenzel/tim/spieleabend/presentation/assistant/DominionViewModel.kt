package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.helpers.Event
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.models.*
import de.stenzel.tim.spieleabend.repositories.DominionRepository
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DominionViewModel @Inject constructor(
    private val repository: DominionRepository
) : ViewModel() {

    private val _dominionAll = MutableLiveData<Event<Resource<DominionModel>>>()
    val dominionAll : LiveData<Event<Resource<DominionModel>>>
        get() = _dominionAll

    private val _preparedDominionData = MutableLiveData<ArrayList<Any>>()
    val preparedDominionExpansionData : LiveData<ArrayList<Any>>
        get() = _preparedDominionData

    private val _generatedDeck = MutableLiveData<Event<Resource<DominionDeck>>>()
    val generatedDeck : LiveData<Event<Resource<DominionDeck>>>
        get() = _generatedDeck


    fun loadData() {
        _dominionAll.postValue(Event(Resource.loading(null)))

        val dominion = repository.getDominionExpansionsWithCards()

        _dominionAll.postValue(Event(dominion))
    }

    fun prepareDataForExpandableAdapter(dominionModel: DominionModel) {
        val finalList = arrayListOf<Any>()

        for (expansion in dominionModel.expansions) {
            finalList.add(ExpandableDominionExpansionModel(expansion))

            val cards = expansion.cards
            for (card in cards) {
                finalList.add(ExpandableDominionCardModel(card))
            }


        }
        _preparedDominionData.postValue(finalList)
    }

    fun generateDeck(list: ArrayList<Any>) {
        //price <= 2
        val cardsCost2 = mutableListOf<DominionModel.Expansion.Card>()
        val cardsCost3 = mutableListOf<DominionModel.Expansion.Card>()
        val cardsCost4 = mutableListOf<DominionModel.Expansion.Card>()
        val cardsCost5 = mutableListOf<DominionModel.Expansion.Card>()
        //price >= 6
        val cardsCost6 = mutableListOf<DominionModel.Expansion.Card>()
        for (item in list) {
            if (item is ExpandableDominionCardModel) {
                if (item.isSelected) {
                    val card = item.card
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
        }

        val curve = getPriceCurve()

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

        val chosenCards = mutableListOf<DominionModel.Expansion.Card>()
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

        val chosenEvent = repository.getRandomEvent()
        val chosenMonument = repository.getRandomMonument()

        val generatedDeck = DominionDeck(chosenCards, chosenEvent, chosenMonument)

        _generatedDeck.postValue(Event(Resource.success(generatedDeck)))
    }

    private fun getPriceCurve() : DominionPriceCurve {
        return repository.getPriceCurve()
    }

}