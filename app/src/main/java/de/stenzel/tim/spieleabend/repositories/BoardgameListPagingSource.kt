package de.stenzel.tim.spieleabend.repositories

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.models.Game
import de.stenzel.tim.spieleabend.network.BoardgameApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val INITIAL_PAGE = 1

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
) : PagingSource<Int, Game>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {

        return try {
            val page = if (params.key == null) {
                INITIAL_PAGE
            } else {
                params.key
            }
            val limit = Constants.NETWORK_PAGE_SIZE
            val offset = (page!!.minus(1)) * limit
            Log.d("BGListPagingSource", "position: $page, offset: $offset")
            val response = service.searchPaging(limit, skip = offset, clientId = Constants.BOARDGAME_API_CLIENT_ID)

            val body = response.body()

            val prevKey = if (page == INITIAL_PAGE) {
                null
            } else {
                page - 1
            }

            //checks if next page can ne loaded since max skip amount is 1000
            val nextKey = if ((page * Constants.NETWORK_PAGE_SIZE) + Constants.NETWORK_PAGE_SIZE < 1000) {
                page + 1
            } else {
                null
            }

            LoadResult.Page(
                data = body!!.games,
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }

    }


    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }




}