package de.stenzel.tim.spieleabend.repositories

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.models.Game
import de.stenzel.tim.spieleabend.network.BoardgameApiService
import javax.inject.Inject

/**
 * provides methods to get all boardgame data from a remote source
 */
class BoardgameRepository @Inject constructor(
    private val service : BoardgameApiService
){

    /**
     * get all boardgames paginated
     * @param defaultConfig
     */
    fun getBoardgames(pagingConfig: PagingConfig = getDefaultConfig()): LiveData<PagingData<Game>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {BoardgameListPagingSource(service)}
        ).liveData
    }

    /**
     * provide a default config for pagination
     */
    private fun getDefaultConfig(): PagingConfig {
        return PagingConfig(
            pageSize = Constants.NETWORK_PAGE_SIZE * 3,
            enablePlaceholders = false
        )
    }

    /**
     * get details of a specific boardgame
     * @param id id of the game
     */
    suspend fun getGameDetail(id: String) = service.getGameById(id, Constants.BOARDGAME_API_CLIENT_ID)

    /**
     * get a list of all categories
     */
    suspend fun getAllCategories() = service.getCategories(Constants.BOARDGAME_API_CLIENT_ID)

    /**
     * get a list of all mechanics
     */
    suspend fun getAllMechanics() = service.getMechanics(Constants.BOARDGAME_API_CLIENT_ID)

}