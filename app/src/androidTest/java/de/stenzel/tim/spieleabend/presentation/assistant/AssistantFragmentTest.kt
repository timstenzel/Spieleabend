package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import de.stenzel.tim.spieleabend.R
import de.stenzel.tim.spieleabend.launchFragmentInHiltContainer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AssistantFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickDominionDeckGeneratorItem_navigateToDominionFragment() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AssistantFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.assistant_rv)).perform(RecyclerViewActions.actionOnItemAtPosition<AssistantAdapter.AssistantItemViewHolder>(0, click()))

        verify(navController).navigate(R.id.action_assistantFragment_to_dominionFragment)
    }

    @Test
    fun clickFirstPlayerItem_navigateToFirstPlayerFragment() {

        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AssistantFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.assistant_rv)).perform(RecyclerViewActions.actionOnItemAtPosition<AssistantAdapter.AssistantItemViewHolder>(1, click()))
        
        verify(navController).navigate(R.id.action_assistantFragment_to_firstPlayerFragment)
    }

    @Test
    fun clickBoardgameCatalogueItem_navigateToCatalogueFragment() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AssistantFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.assistant_rv)).perform(RecyclerViewActions.actionOnItemAtPosition<AssistantAdapter.AssistantItemViewHolder>(2, click()))

        verify(navController).navigate(R.id.action_assistantFragment_to_catalogueFragment)
    }

}