package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.models.Game
import de.stenzel.tim.spieleabend.repositories.BoardgameRepository
import javax.inject.Inject

@HiltViewModel
class CatalogueViewModel @Inject constructor(
    private val boardgameRepository: BoardgameRepository
) : ViewModel() {

    private var paginatedLiveData : MutableLiveData<PagingData<Game>>? = null

    fun fetchBoardgames(): LiveData<PagingData<Game>> {
        if (paginatedLiveData != null) {
            return paginatedLiveData as MutableLiveData<PagingData<Game>>
        } else {
            return boardgameRepository.getBoardgames().cachedIn(viewModelScope)
        }
    }


}