package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.models.BoardgameWrapper
import de.stenzel.tim.spieleabend.repositories.BoardgameRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogueDetailViewModel @Inject constructor(
    private val repository: BoardgameRepository
) : ViewModel() {

    private val _game = MutableLiveData<BoardgameWrapper>()
    val game : LiveData<BoardgameWrapper>
        get() = _game

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    fun loadGameDetails(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val response = repository.getGameDetail(id)
            if (response.isSuccessful) {
                _game.value = response.body()
                _isLoading.value = false
            } else {
                // TODO: 07.10.21 handle error
                _isLoading.value = false
            }
        }
    }
}