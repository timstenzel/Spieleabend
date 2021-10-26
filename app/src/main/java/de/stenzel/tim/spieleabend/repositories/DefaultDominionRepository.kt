package de.stenzel.tim.spieleabend.repositories

import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.models.DominionModel
import de.stenzel.tim.spieleabend.models.DominionPriceCurve
import kotlin.random.Random

class DefaultDominionRepository : DominionRepository {

    override fun getDominionExpansionsWithCards(): Resource<DominionModel> {
        val ex1Cards = mutableListOf<DominionModel.Expansion.Card>()
        ex1Cards.add(DominionModel.Expansion.Card(1001, "Burggraben", 2, ""))
        ex1Cards.add(DominionModel.Expansion.Card(1002, "Kapelle", 2, ""))
        ex1Cards.add(DominionModel.Expansion.Card(1003, "Keller", 3, ""))
        ex1Cards.add(DominionModel.Expansion.Card(1004, "Dorf", 4, ""))
        ex1Cards.add(DominionModel.Expansion.Card(1005, "Händlerin", 5, ""))
        ex1Cards.add(DominionModel.Expansion.Card(1006, "Vasall", 6, ""))

        val ex2Cards = mutableListOf<DominionModel.Expansion.Card>()
        ex2Cards.add(DominionModel.Expansion.Card(2001, "Burg", 2, ""))
        ex2Cards.add(DominionModel.Expansion.Card(2002, "Kirche", 2, ""))
        ex2Cards.add(DominionModel.Expansion.Card(2003, "Markt", 3, ""))
        ex2Cards.add(DominionModel.Expansion.Card(2004, "Diener", 4, ""))
        ex2Cards.add(DominionModel.Expansion.Card(2005, "Ritter", 5, ""))
        ex2Cards.add(DominionModel.Expansion.Card(2006, "König", 6, ""))

        val ex3Cards = mutableListOf<DominionModel.Expansion.Card>()
        ex3Cards.add(DominionModel.Expansion.Card(3001, "Zugbrücke", 3, ""))
        ex3Cards.add(DominionModel.Expansion.Card(3002, "Schild", 4, ""))
        ex3Cards.add(DominionModel.Expansion.Card(3003, "Schwert", 5, ""))
        ex3Cards.add(DominionModel.Expansion.Card(3004, "Königin", 6, ""))
        ex3Cards.add(DominionModel.Expansion.Card(3005, "Prinz", 5, ""))
        ex3Cards.add(DominionModel.Expansion.Card(3006, "Vorkoster", 4, ""))

        val ex1 = DominionModel.Expansion(1, "Basis", ex1Cards)
        val ex2 = DominionModel.Expansion(2, "Abenteuer", ex2Cards)
        val ex3 = DominionModel.Expansion(3, "Intriege", ex3Cards)

        val expansions = listOf(ex1, ex2, ex3)

        val dominion = DominionModel(expansions)

        return Resource.success(dominion)
    }

    override fun getPriceCurve(): DominionPriceCurve {
        val curve1 = DominionPriceCurve(2,2,2,2,2)
        val curve2 = DominionPriceCurve(1,3,3,2,1)
        val curve3 = DominionPriceCurve(2,2,3,2,1)

        val curves = listOf(curve1, curve2, curve3)

        val r = Random.nextInt(0, curves.size)

        return curves[r]
    }

    override fun getRandomEvent(): DominionModel.Expansion.Card {
        val events = mutableListOf<DominionModel.Expansion.Card>()
        events.add(DominionModel.Expansion.Card(10001, "Ereignis1", 0, ""))
        events.add(DominionModel.Expansion.Card(10002, "Ereignis2", 0, ""))
        events.add(DominionModel.Expansion.Card(10003, "Ereignis3", 0, ""))
        events.add(DominionModel.Expansion.Card(10004, "Ereignis4", 0, ""))
        events.add(DominionModel.Expansion.Card(10005, "Ereignis5", 0, ""))
        events.add(DominionModel.Expansion.Card(10006, "Ereignis6", 0, ""))
        events.add(DominionModel.Expansion.Card(10007, "Ereignis7", 0, ""))

        val r = Random.nextInt(0, events.size)

        return events[r]
    }

    override fun getRandomMonument(): DominionModel.Expansion.Card {
        val monuments = mutableListOf<DominionModel.Expansion.Card>()
        monuments.add(DominionModel.Expansion.Card(11001, "Monument1", 0, ""))
        monuments.add(DominionModel.Expansion.Card(11002, "Monument2", 0, ""))
        monuments.add(DominionModel.Expansion.Card(11003, "Monument3", 0, ""))
        monuments.add(DominionModel.Expansion.Card(11004, "Monument4", 0, ""))
        monuments.add(DominionModel.Expansion.Card(11005, "Monument5", 0, ""))
        monuments.add(DominionModel.Expansion.Card(11006, "Monument6", 0, ""))
        monuments.add(DominionModel.Expansion.Card(11007, "Monument7", 0, ""))

        val r = Random.nextInt(0, monuments.size)

        return monuments[r]
    }
}