package de.stenzel.tim.spieleabend.network

import de.stenzel.tim.spieleabend.helpers.Constants
import de.stenzel.tim.spieleabend.models.remote.BoardgameResponse
import de.stenzel.tim.spieleabend.models.remote.CategoriesResponse
import de.stenzel.tim.spieleabend.models.remote.MechanicsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BoardgameApiService {

    @GET(Constants.END_POINT_SEARCH)
    suspend fun searchPaging(
        @Query("limit") limit: Int = Constants.NETWORK_PAGE_SIZE,
        @Query("skip") skip: Int = 0,
        @Query("client_id") clientId: String = Constants.BOARDGAME_API_CLIENT_ID
    ) : Response<BoardgameResponse>

    @GET(Constants.END_POINT_SEARCH)
    suspend fun getGameById(
        @Query("ids") id: String,
        @Query("client_id") clientId: String = Constants.BOARDGAME_API_CLIENT_ID
    ) : Response<BoardgameResponse>

    @GET(Constants.END_POINT_CATEGORIES)
    suspend fun getCategories(
        @Query("client_id") clientId: String = Constants.BOARDGAME_API_CLIENT_ID
    ) : Response<CategoriesResponse>

    @GET(Constants.END_POINT_MECHANICS)
    suspend fun getMechanics(
        @Query("client_id") clientId: String = Constants.BOARDGAME_API_CLIENT_ID
    ) : Response<MechanicsResponse>

}