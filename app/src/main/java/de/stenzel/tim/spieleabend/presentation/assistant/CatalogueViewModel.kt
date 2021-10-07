package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.models.BoardgameWrapper2
import de.stenzel.tim.spieleabend.repositories.BoardgameRepository
import javax.inject.Inject

@HiltViewModel
class CatalogueViewModel @Inject constructor(
    private val boardgameRepository: BoardgameRepository
) : ViewModel() {

    fun fetchBoardgames(): LiveData<PagingData<BoardgameWrapper2.Game>> {
        return boardgameRepository.getBoardgames().cachedIn(viewModelScope)
    }


}