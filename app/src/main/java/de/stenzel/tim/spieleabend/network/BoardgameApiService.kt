package de.stenzel.tim.spieleabend.network

import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.models.BoardgameWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BoardgameApiService {

    @GET(Constants.END_POINT_SEARCH)
    suspend fun searchPaging(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
        @Query("client_id") clientId: String
    ) : Response<BoardgameWrapper>

    @GET(Constants.END_POINT_SEARCH)
    suspend fun getGameById(
        @Query("ids") id: String,
        @Query("client_id") clientId: String
    ) : Response<BoardgameWrapper>

}