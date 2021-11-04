package de.stenzel.tim.spieleabend.models.remote

/**
* data class mirroring a json response from boardgameatlas.com api
*/
data class BoardgameResponse(
    val games : List<Game>,
    val status: String
)
