package de.stenzel.tim.spieleabend.repositories

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.models.BoardgameWrapper
import de.stenzel.tim.spieleabend.network.BoardgameApiService
import de.stenzel.tim.spieleabend.network.BoardgameListPagingSource
import javax.inject.Inject

class BoardgameRepository @Inject constructor(
    private val service : BoardgameApiService
){
    fun getBoardgameList(): LiveData<PagingData<BoardgameWrapper.Game>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BoardgameListPagingSource(service)
            }
        ).liveData
    }
}