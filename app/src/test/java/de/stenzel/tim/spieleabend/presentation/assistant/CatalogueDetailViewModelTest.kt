package de.stenzel.tim.spieleabend.presentation.assistant

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import de.stenzel.tim.spieleabend.MainCoroutineRule
import de.stenzel.tim.spieleabend.getOrAwaitValueTest
import de.stenzel.tim.spieleabend.helpers.Status
import de.stenzel.tim.spieleabend.repositories.FakeBoardgameRespositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CatalogueDetailViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CatalogueDetailViewModel
    private lateinit var repository: FakeBoardgameRespositoryImpl

    @Before
    fun setup() {
        repository = FakeBoardgameRespositoryImpl()
        viewModel = CatalogueDetailViewModel(repository = repository)
    }

    @Test
    fun `download mechanics, categories and game details, all return success`() {

        viewModel.getBoardgameDetails("")

        val value = viewModel.game.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `download mechanics, categories and game details, return error`() {

        repository.setShouldReturnNetworkError(true)

        viewModel.getBoardgameDetails("")

        val value = viewModel.game.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

    }


}