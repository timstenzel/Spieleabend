package de.stenzel.tim.spieleabend.models

/**
* data class mirroring a json response from boardgameatlas.com api
* unneccessary fields are commented, but left for possible future use
*/
data class BoardgameWrapper(
    val games : List<Game>
)
