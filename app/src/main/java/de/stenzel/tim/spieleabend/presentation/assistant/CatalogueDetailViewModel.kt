package de.stenzel.tim.spieleabend.presentation.assistant

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.models.Category
import de.stenzel.tim.spieleabend.models.Game
import de.stenzel.tim.spieleabend.models.Mechanic
import de.stenzel.tim.spieleabend.repositories.BoardgameRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CatalogueDetailViewModel @Inject constructor(
    private val repository: BoardgameRepository
) : ViewModel() {

    private val TAG = "CDVM"

    private val _game = MutableLiveData<Game>()
    val game : LiveData<Game>
        get() = _game

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    suspend fun loadGameDetails(id: String) {
        _isLoading.value = true

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

            Log.d("CDVM", "trigger update")
            _game.value = gameById
            _isLoading.value = false
        } else {
            _isLoading.value = false
            //handle error
        }

        /*
        viewModelScope.launch {
            val mechanicsResponse = repository.getAllMechanics()
            if (mechanicsResponse.isSuccessful) {
                val categoriesResponse = repository.getAllCategories()
                if (categoriesResponse.isSuccessful) {
                    val gameResponse = repository.getGameDetail(id)
                    if (gameResponse.isSuccessful) {
                        val mechanicsBody = mechanicsResponse.body()!!
                        val listM = mechanicsBody.mechanics
                        val mapM = listM.associateBy({mechanic -> mechanic.id}, {mechanic -> mechanic.name })

                        val categoriesBody = categoriesResponse.body()!!
                        val listC = categoriesBody.categories
                        val mapC = listC.associateBy({category -> category.id}, {category -> category.name })

                        val gameBody = gameResponse.body()!!
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

                        Log.d("CDVM", "trigger update")
                        _game.value = gameById
                        _isLoading.value = false
                    } else {
                        _isLoading.value = false
                        //handle error
                    }
                }
            }

         */


            /*
            val response = repository.getGameDetail(id)
            if (response.isSuccessful) {
                val body = response.body()
                val games = body!!.games
                val gameById = games.first()
                /*
                val gameCats = gameById.categories
                val catList = arrayListOf<Category>()
                for (g in gameCats) {
                    catList.add(Category(g.id, mapC[g.id].toString(), g.url))
                }
                gameById.categories = catList

                val gameMecs = gameById.mechanics
                val mecList = arrayListOf<Mechanic>()
                for (g in gameMecs) {
                    mecList.add(Mechanic(g.id, mapM[g.id].toString(), g.url))
                }
                gameById.mechanics = mecList

                 */

                _game.value = gameById
                _isLoading.value = false
            } else {
                // TODO: 07.10.21 handle error
                _isLoading.value = false
            }

        }
             */
    }
}