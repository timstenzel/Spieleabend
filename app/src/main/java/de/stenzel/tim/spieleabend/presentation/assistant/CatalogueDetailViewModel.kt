package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.helpers.Event
import de.stenzel.tim.spieleabend.helpers.Resource
import de.stenzel.tim.spieleabend.helpers.Status
import de.stenzel.tim.spieleabend.models.remote.*
import de.stenzel.tim.spieleabend.repositories.BoardgameRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CatalogueDetailViewModel @Inject constructor(
    private val repository: BoardgameRepository
) : ViewModel() {

    private val TAG = "CDVM"

    private val _game = MutableLiveData<Event<Resource<Game>>>()
    val game : LiveData<Event<Resource<Game>>>
        get() = _game


    fun getBoardgameDetails(id: String) = viewModelScope.launch {
        safeLoadGameDetails(id)
    }

    private suspend fun safeLoadGameDetails(id: String) {

        _game.postValue(Event(Resource.loading(null)))

        val mechRes = withContext(viewModelScope.coroutineContext) {
            repository.getMechanics()
        }

        val catRes = withContext(viewModelScope.coroutineContext) {
            repository.getCategories()
        }

        val gameRes = withContext(viewModelScope.coroutineContext) {
            repository.getGameDetail(id)
        }

        _game.postValue(Event(handleResponses(mechRes, catRes, gameRes)))
    }

    private fun handleResponses(mechRes: Resource<MechanicsResponse>, catRes: Resource<CategoriesResponse>, gameRes: Resource<BoardgameResponse>) : Resource<Game> {

        if (mechRes.status == Status.SUCCESS && catRes.status == Status.SUCCESS && gameRes.status == Status.SUCCESS) {

            val mechanicsBody = mechRes.data!!
            val listM = mechanicsBody.mechanics
            val mapM = listM.associateBy({mechanic -> mechanic.id}, {mechanic -> mechanic.name })

            val categoriesBody = catRes.data!!
            val listC = categoriesBody.categories
            val mapC = listC.associateBy({category -> category.id}, {category -> category.name })

            val gameBody = gameRes.data!!
            val listG = gameBody.games
            if (listG.isEmpty()) {
                return Resource.error("no game in list", null)
            } else {
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

                return Resource.success(gameById)
            }
        } else {
            //handle error
            return Resource.error("Could not get all data", null)
        }

    }
}