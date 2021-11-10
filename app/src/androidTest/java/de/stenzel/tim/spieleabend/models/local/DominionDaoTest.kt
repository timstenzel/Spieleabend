package de.stenzel.tim.spieleabend.models.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import de.stenzel.tim.spieleabend.launchFragmentInHiltContainer
import de.stenzel.tim.spieleabend.presentation.assistant.AssistantFragment
import de.stenzel.tim.spieleabend.presentation.assistant.DominionFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class DominionDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: DominionDatabase

    private lateinit var dao: DominionDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.dominionDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertExpansions() = runBlockingTest {
        val exp1 = DominionExpansion(1, "exp1")
        val exp2 = DominionExpansion(2, "exp2")
        val expansions = listOf(exp1, exp2)

        dao.insertAllExpansions(expansions)

        val allExpansions = dao.getExpansions()

        assertThat(allExpansions).containsExactly(exp1, exp2)
    }

    @Test
    fun insertCards() = runBlockingTest {
        //first insert an expansion because event contains the expansion id as a foreign key
        val exp1 = DominionExpansion(1, "exp1")
        val expansions = listOf(exp1)
        dao.insertAllExpansions(expansions)
        //next, insert the cards with the expansion id used above
        val c1 = DominionCard(1, "title", 1, "text", 1)
        val c2 = DominionCard(2, "title", 1, "text", 1)
        val cards = listOf(c1, c2)

        dao.insertAllCards(cards)

        val allCards = dao.getCards()

        assertThat(allCards).containsExactly(c1, c2)
    }

    @Test
    fun insertEvents() = runBlockingTest {
        //first insert an expansion because event contains the expansion id as a foreign key
        val exp1 = DominionExpansion(1, "exp1")
        val expansions = listOf(exp1)
        dao.insertAllExpansions(expansions)
        //next, insert the events with die expansion id used above
        val ev1 = DominionEvent(1, "title", 1, "text", 1)
        val ev2 = DominionEvent(2, "title", 1, "text", 1)
        val events = listOf(ev1, ev2)

        dao.insertAllEvents(events)

        val allEvents = dao.getEvents()

        assertThat(allEvents).containsExactly(ev1, ev2)
    }

    @Test
    fun insertLandmarks() = runBlockingTest {
        //first insert an expansion because event contains the expansion id as a foreign key
        val exp1 = DominionExpansion(1, "exp1")
        val expansions = listOf(exp1)
        dao.insertAllExpansions(expansions)
        //next, insert the events with die expansion id used above
        val lm1 = DominionLandmark(1, "title", 1, "text", 1)
        val lm2 = DominionLandmark(2, "title", 1, "text", 1)
        val landmarks = listOf(lm1, lm2)

        dao.insertAllLandmarks(landmarks)

        val allLandmarks = dao.getLandmarks()

        assertThat(allLandmarks).containsExactly(lm1, lm2)
    }

    @Test
    fun insertPriceCurves() = runBlockingTest {
        val pc1 = DominionPriceCurve(1, 2, 3, 3, 1, 1)
        val pc2 = DominionPriceCurve(2, 1, 3, 3, 2, 1)
        val curves = listOf(pc1, pc2)

        dao.insertAllPriceCurves(curves)

        val allPriceCurves = dao.getPriceCurves()

        assertThat(allPriceCurves).containsExactly(pc1, pc2)
    }
}