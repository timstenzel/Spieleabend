package de.stenzel.tim.spieleabend.helpers

import de.stenzel.tim.spieleabend.models.local.*

fun expansionsForPrepopulation() : List<DominionExpansion> {
    val expansions = mutableListOf<DominionExpansion>()
    expansions.add(DominionExpansion(1, "Basis"))
    expansions.add(DominionExpansion(2, "Abenteuer"))
    expansions.add(DominionExpansion(3, "Intriege"))

    return expansions
}

fun cardsForPrepopulation() : List<DominionCard> {
    val cards = mutableListOf<DominionCard>()
    cards.add(DominionCard(1001, "Burggraben", 2, "", 1))
    cards.add(DominionCard(1002, "Kapelle", 2, "", 1))
    cards.add(DominionCard(1003, "Keller", 3, "", 1))
    cards.add(DominionCard(1004, "Dorf", 4, "", 1))
    cards.add(DominionCard(1005, "Händlerin", 5, "", 1))
    cards.add(DominionCard(1006, "Vasall", 6, "", 1))

    cards.add(DominionCard(2001, "Burg", 2, "", 2))
    cards.add(DominionCard(2002, "Kirche", 2, "", 2))
    cards.add(DominionCard(2003, "Markt", 3, "", 2))
    cards.add(DominionCard(2004, "Diener", 4, "", 2))
    cards.add(DominionCard(2005, "Ritter", 5, "", 2))
    cards.add(DominionCard(2006, "König", 6, "", 2))

    cards.add(DominionCard(3001, "Zugbrücke", 3, "", 3))
    cards.add(DominionCard(3002, "Schild", 4, "", 3))
    cards.add(DominionCard(3003, "Schwert", 4, "", 3))
    cards.add(DominionCard(3004, "Königin", 5, "", 3))
    cards.add(DominionCard(3005, "Prinz", 5, "", 3))
    cards.add(DominionCard(3006, "Vorkoster", 6, "", 3))

    return cards
}

fun priceCurvesForPrepopulation(): List<DominionPriceCurve> {
    val curves = mutableListOf<DominionPriceCurve>()

    curves.add(DominionPriceCurve(2,2,2,2,2, 2))
    curves.add(DominionPriceCurve(1,3,3,2,1, 1))
    curves.add(DominionPriceCurve(2,2,3,2,1, 2))

    return curves
}

fun eventsForPrepopulation(): List<DominionEvent> {
    val events = mutableListOf<DominionEvent>()
    events.add(DominionEvent(10001, "Ereignis1", 0, "", 1))
    events.add(DominionEvent(10002, "Ereignis2", 0, "", 1))
    events.add(DominionEvent(10003, "Ereignis3", 0, "", 2))
    events.add(DominionEvent(10004, "Ereignis4", 0, "", 2))
    events.add(DominionEvent(10005, "Ereignis5", 0, "", 3))
    events.add(DominionEvent(10006, "Ereignis6", 0, "", 3))
    events.add(DominionEvent(10007, "Ereignis7", 0, "", 3))

    return events
}

fun landmarksForPrepopulation(): List<DominionLandmark> {
    val landmarks = mutableListOf<DominionLandmark>()
    landmarks.add(DominionLandmark(11001, "Monument1", 0, "", 1))
    landmarks.add(DominionLandmark(11002, "Monument2", 0, "", 1))
    landmarks.add(DominionLandmark(11003, "Monument3", 0, "", 2))
    landmarks.add(DominionLandmark(11004, "Monument4", 0, "", 2))
    landmarks.add(DominionLandmark(11005, "Monument5", 0, "", 3))
    landmarks.add(DominionLandmark(11006, "Monument6", 0, "", 3))

    return landmarks
}
