package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.stenzel.tim.spieleabend.models.remote.Game
import de.stenzel.tim.spieleabend.repositories.DefaultBoardgameRepository
import javax.inject.Inject

@HiltViewModel
class CatalogueViewModel @Inject constructor(
    private val repository: DefaultBoardgameRepository
) : ViewModel() {

    var paginatedLiveData : MutableLiveData<PagingData<Game>>? = null

    suspend fun fetchBoardgames(): LiveData<PagingData<Game>> {

        if (paginatedLiveData != null) {
            return paginatedLiveData as MutableLiveData<PagingData<Game>>
        } else {
            return repository.searchGamesPaging().cachedIn(viewModelScope)
        }
    }


}