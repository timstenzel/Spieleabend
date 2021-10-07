package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.models.BoardgameWrapper
import de.stenzel.tim.spieleabend.repositories.BoardgameRepository
import javax.inject.Inject

@HiltViewModel
class CatalogueViewModel @Inject constructor(
    private val boardgameRepository: BoardgameRepository
) : ViewModel() {

    private val _boardgames = MutableLiveData<PagingData<BoardgameWrapper.Game>>()

    fun getBoardgameList(): LiveData<PagingData<BoardgameWrapper.Game>> {
        val response = boardgameRepository.getBoardgameList().cachedIn(viewModelScope)
        _boardgames.value = response.value
        return response
    }


}