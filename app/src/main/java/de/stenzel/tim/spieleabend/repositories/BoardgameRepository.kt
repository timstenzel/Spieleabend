package de.stenzel.tim.spieleabend.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.models.remote.BoardgameResponse
import de.stenzel.tim.spieleabend.models.remote.CategoriesResponse
import de.stenzel.tim.spieleabend.models.remote.Game
import de.stenzel.tim.spieleabend.models.remote.MechanicsResponse

interface BoardgameRepository {

    suspend fun searchGamesPaging(): LiveData<PagingData<Game>>

    suspend fun getGameDetail(id: String): Resource<BoardgameResponse>

    suspend fun getMechanics() : Resource<MechanicsResponse>

    suspend fun getCategories() : Resource<CategoriesResponse>
}