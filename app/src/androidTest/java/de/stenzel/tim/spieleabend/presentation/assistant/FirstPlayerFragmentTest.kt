package de.stenzel.tim.spieleabend.presentation.assistant

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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
import org.mockito.Mockito
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class FirstPlayerFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun editPlayerAmount_clickConfirm_navigateToLuckyWheelFragment() {

        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<FirstPlayerFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        val amount = 8
        onView(withId(R.id.first_player_amount_et)).perform(clearText(), typeText(amount.toString()))
        closeSoftKeyboard()
        onView(withId(R.id.first_player_confirm_btn)).perform(click())

        val action = FirstPlayerFragmentDirections.actionFirstPlayerFragmentToLuckyWheelFragment(amount)
        verify(navController).navigate(action)
    }

}