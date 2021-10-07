package de.stenzel.tim.spieleabend.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.models.BoardgameWrapper
import javax.inject.Inject

private const val INITIAL_SKIP_SIZE = 0

/**
 * Class getting the data from the boardgameatlas api by pages
 * limit = amount of objects to get (see constants file for current value)
 * start by getting 3*limit amount of data
 * with the second request: get 1*limit
 * set offset to limit + 1 to get the next data (= skip the already obtained data)
 * 7. Oct. 21: Api only allows a skip value of 1000 entries so if the data is larger entry 1001+ can not be shown
 */
class BoardgameListPagingSource @Inject constructor(
    private val service: BoardgameApiService,
) : PagingSource<Int, BoardgameWrapper.Game>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BoardgameWrapper.Game> {
        val position = when (params.key) {
            null -> INITIAL_SKIP_SIZE
            else -> params.key
        }

        val offset = if (params.key != null) {
            position!! + 1
        } else {
            INITIAL_SKIP_SIZE
        }

        Log.d("BGListPagingSource", "position: $position, offset: $offset")

        return try {
            val wrapper = service.searchPaging(limit = Constants.NETWORK_PAGE_SIZE, skip = offset, clientId = Constants.BOARDGAME_API_CLIENT_ID)
            val response = wrapper.body()

            val nextKey = if (response?.games?.isEmpty() == true) {
                null
            } else {
                position!! + params.loadSize
            }
            LoadResult.Page(
                data = response!!.games,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }


    override fun getRefreshKey(state: PagingState<Int, BoardgameWrapper.Game>): Int? {
        return null
    }




}