package de.stenzel.tim.spieleabend.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.models.*

class FakeBoardgameRespositoryImpl : BoardgameRepository{

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun searchGamesPaging(): LiveData<PagingData<Game>> {
        TODO("Not yet implemented")
    }

    override suspend fun getGameDetail(id: String): Resource<BoardgameResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error from network", null)
        } else {
            Resource.success(BoardgameResponse(listOf(createGame()), ""))
        }
    }

    override suspend fun getMechanics(): Resource<MechanicsResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error from network", null)
        } else {
            Resource.success(MechanicsResponse(listOf()))
        }
    }

    override suspend fun getCategories(): Resource<CategoriesResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error from network", null)
        } else {
            Resource.success(CategoriesResponse(listOf()))
        }
    }

    private fun createGame(): Game {
        return Game(
            id = "",
            handle =  "",
            url =  "",
            name =  "",
            price =  "",
            discount =  "",
            year_published =  0,
            min_players =  0,
            max_players =  0,
            min_playtime =  0,
            max_playtime =  0,
            min_age =  0,
            description =  "",
            faq =  "",
            thumb_url =  "",
            image_url =  "",
            mechanics = listOf(),
            categories = listOf(),
            publishers = listOf(),
            designers = listOf(),
            primary_publisher = PrimaryPublisher("", "", ""),
            primary_designer = PrimaryDesigner("", "", ""),
            rules_url =  "",
            amazon_rank =  0,
            official_url =  "",
            num_user_ratings =  0,
            average_user_rating =  0.0,
            weight_amount =  0.0,
            weight_units =  "",
            size_height =  0.0,
            size_depth =  0.0,
            size_units =  "",
            num_user_complexity_votes =  0,
            average_learning_complexity =  0.0,
            average_strategy_complexity =  0.0,
            images = Images("", "", "", "", "")
        )
    }

}