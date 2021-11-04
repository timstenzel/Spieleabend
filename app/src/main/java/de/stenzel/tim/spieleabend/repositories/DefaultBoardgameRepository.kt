package de.stenzel.tim.spieleabend.repositories

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.models.remote.BoardgameResponse
import de.stenzel.tim.spieleabend.models.remote.CategoriesResponse
import de.stenzel.tim.spieleabend.models.remote.Game
import de.stenzel.tim.spieleabend.models.remote.MechanicsResponse
import de.stenzel.tim.spieleabend.network.BoardgameApiService
import java.lang.Exception
import javax.inject.Inject

/**
 * provides methods to get all boardgame data from a remote source
 */
class DefaultBoardgameRepository @Inject constructor(
    private val service : BoardgameApiService
) : BoardgameRepository {

    /**
     * get all boardgames paginated
     * @param pagingConfig
     */
    override suspend fun searchGamesPaging(): LiveData<PagingData<Game>> {
        return Pager(
            config = getDefaultConfig(),
            pagingSourceFactory = {BoardgameListPagingSource(service)}
        ).liveData
    }

    /**
     * provide a default config for pagination
     */
    private fun getDefaultConfig(): PagingConfig {
        return PagingConfig(
            pageSize = Constants.NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        )
    }

    /**
     * get details of a specific boardgame
     * @param id id of the game
     */
    override suspend fun getGameDetail(id: String): Resource<BoardgameResponse> {
        return try {
            val response = service.getGameById(id)
            if (response.isSuccessful) {
                response.body()?.let { games ->
                    Resource.success(games)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach server, check internet connection.", null)
        }
    }

    /**
     * get a list of all mechanics
     */
    override suspend fun getMechanics(): Resource<MechanicsResponse> {
        return try {
            val response = service.getMechanics()
            if (response.isSuccessful) {
                response.body()?.let { mechanics ->
                    Resource.success(mechanics)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach server, check internet connection.", null)
        }
    }

    /**
     * get a list of all categories
     */
    override suspend fun getCategories(): Resource<CategoriesResponse> {
        return try {
            val response = service.getCategories()
            if (response.isSuccessful) {
                response.body()?.let { categories ->
                    Resource.success(categories)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Could not reach server, check internet connection.", null)
        }
    }
}