package de.stenzel.tim.spieleabend.presentation.assistant

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.helpers.isNetworkAvailable
import de.stenzel.tim.spieleabend.models.*
import de.stenzel.tim.spieleabend.repositories.BoardgameRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CatalogueDetailViewModel @Inject constructor(
    private val application: Application,
    private val repository: BoardgameRepository
) : ViewModel() {

    private val TAG = "CDVM"

    val game : MutableLiveData<Resource<Game>> = MutableLiveData()

    fun getBoardgameDetails(id: String) = viewModelScope.launch {
        safeLoadGameDetails(id)
    }

    private suspend fun safeLoadGameDetails(id: String) {

        try {
            if (isNetworkAvailable(application)) {
                game.postValue(Resource.Loading())

                Log.d(TAG, "start mechanics")
                val mechRes = withContext(viewModelScope.coroutineContext) {
                    repository.getAllMechanics()
                }
                Log.d(TAG, "start categories")
                val catRes = withContext(viewModelScope.coroutineContext) {
                    repository.getAllCategories()
                }
                Log.d(TAG, "start game")
                val gameRes = withContext(viewModelScope.coroutineContext) {
                    repository.getGameDetail(id)
                }

                game.postValue(handleResponses(mechRes, catRes, gameRes))

            } else {
                game.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    game.postValue(Resource.Error("Network failure"))
                }
                else -> {
                    game.postValue(Resource.Error("Conversion error"))
                }
            }
        }

    }

    private fun handleResponses(mechRes: Response<MechanicsResponse>, catRes: Response<CategoriesResponse>, gameRes: Response<BoardgameResponse>) : Resource<Game> {

        if (mechRes.isSuccessful && catRes.isSuccessful && gameRes.isSuccessful) {
            Log.d(TAG, "all successful")
            val mechanicsBody = mechRes.body()!!
            val listM = mechanicsBody.mechanics
            val mapM = listM.associateBy({mechanic -> mechanic.id}, {mechanic -> mechanic.name })

            val categoriesBody = catRes.body()!!
            val listC = categoriesBody.categories
            val mapC = listC.associateBy({category -> category.id}, {category -> category.name })

            val gameBody = gameRes.body()!!
            val listG = gameBody.games
            val gameById = listG.first()
            val categoriesOfGame = gameById.categories
            val finalCatList = arrayListOf<Category>()

            for (c in categoriesOfGame) {
                finalCatList.add(Category(c.id, mapC[c.id].toString(), c.url))
            }
            gameById.categories = finalCatList

            val mechanicsOfGame = gameById.mechanics
            val finalMechanicsList = arrayListOf<Mechanic>()

            for (m in mechanicsOfGame) {
                finalMechanicsList.add(Mechanic(m.id, mapM[m.id].toString(), m.url))
            }
            gameById.mechanics = finalMechanicsList

            return Resource.Success(gameById)
        } else {
            //handle error
            return Resource.Error("Could not get all data")
        }

    }
}