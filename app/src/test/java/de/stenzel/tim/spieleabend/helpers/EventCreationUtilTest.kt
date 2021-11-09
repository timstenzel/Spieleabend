package de.stenzel.tim.spieleabend.helpers

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.*

class EventCreationUtilTest {

    @Test
    fun `empty title returns error`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "",
            "description",
            "teststreet, 12345 testlocation",
            "12.345",
            "9.876",
            calendarNow,
            "12.12.21",
            "10:30",
            calendarStart,
            "12.12.21",
            "20:30",
            calendarEnd
        )

        assertThat(result).contains(EventCreationUtil.FormErrors.MISSING_TITLE)
    }

    @Test
    fun `empty description returns error`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "title",
            "",
            "teststreet, 12345 testlocation",
            "12.345",
            "9.876",
            calendarNow,
            "12.12.21",
            "10:30",
            calendarStart,
            "12.12.21",
            "20:30",
            calendarEnd
        )

        assertThat(result).contains(EventCreationUtil.FormErrors.MISSING_DESCRIPTION)
    }

    @Test
    fun `empty location returns error`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "title",
            "description",
            "",
            "12.345",
            "9.876",
            calendarNow,
            "12.12.21",
            "10:30",
            calendarStart,
            "12.12.21",
            "20:30",
            calendarEnd
        )

        assertThat(result).contains(EventCreationUtil.FormErrors.MISSING_LOCATION)
    }

    @Test
    fun `empty latitude returns error`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "title",
            "description",
            "teststreet, 12345 testlocation",
            "",
            "9.876",
            calendarNow,
            "12.12.21",
            "10:30",
            calendarStart,
            "12.12.21",
            "20:30",
            calendarEnd
        )

        assertThat(result).contains(EventCreationUtil.FormErrors.MISSING_LAT)
    }

    @Test
    fun `empty longitude returns error`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "title",
            "description",
            "teststreet, 12345 testlocation",
            "12.345",
            "",
            calendarNow,
            "12.12.21",
            "10:30",
            calendarStart,
            "12.12.21",
            "20:30",
            calendarEnd
        )

        assertThat(result).contains(EventCreationUtil.FormErrors.MISSING_LON)
    }

    @Test
    fun `empty start date returns error`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "title",
            "description",
            "teststreet, 12345 testlocation",
            "12.345",
            "9.876",
            calendarNow,
            "",
            "10:30",
            calendarStart,
            "12.12.21",
            "20:30",
            calendarEnd
        )

        assertThat(result).contains(EventCreationUtil.FormErrors.MISSING_START_DATE)
    }

    @Test
    fun `empty start time returns error`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "title",
            "description",
            "teststreet, 12345 testlocation",
            "12.345",
            "9.876",
            calendarNow,
            "12.12.21",
            "",
            calendarStart,
            "12.12.21",
            "20:30",
            calendarEnd
        )

        assertThat(result).contains(EventCreationUtil.FormErrors.MISSING_START_TIME)
    }

    @Test
    fun `empty end date returns error`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "title",
            "description",
            "teststreet, 12345 testlocation",
            "12.345",
            "9.876",
            calendarNow,
            "12.12.21",
            "10:30",
            calendarStart,
            "",
            "20:30",
            calendarEnd
        )

        assertThat(result).contains(EventCreationUtil.FormErrors.MISSING_END_DATE)
    }

    @Test
    fun `empty end time returns error`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "title",
            "description",
            "teststreet, 12345 testlocation",
            "12.345",
            "9.876",
            calendarNow,
            "12.12.21",
            "10:30",
            calendarStart,
            "12.12.21",
            "",
            calendarEnd
        )

        assertThat(result).contains(EventCreationUtil.FormErrors.MISSING_END_TIME)
    }

    @Test
    fun `start in past returns error`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, -1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "title",
            "description",
            "teststreet, 12345 testlocation",
            "12.345",
            "9.876",
            calendarNow,
            "12.12.21",
            "10:30",
            calendarStart,
            "12.12.21",
            "20:30",
            calendarEnd
        )

        assertThat(result).contains(EventCreationUtil.FormErrors.START_IN_PAST)
    }

    @Test
    fun `end in past returns error`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, -1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "title",
            "description",
            "teststreet, 12345 testlocation",
            "12.345",
            "9.876",
            calendarNow,
            "12.12.21",
            "10:30",
            calendarStart,
            "12.12.21",
            "20:30",
            calendarEnd
        )

        assertThat(result).contains(EventCreationUtil.FormErrors.END_IN_PAST)
    }

    @Test
    fun `end before start returns error`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, -1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "title",
            "description",
            "teststreet, 12345 testlocation",
            "12.345",
            "9.876",
            calendarNow,
            "12.12.21",
            "10:30",
            calendarStart,
            "12.12.21",
            "20:30",
            calendarEnd
        )

        assertThat(result).contains(EventCreationUtil.FormErrors.END_BEFORE_START)
    }

    @Test
    fun `all valid returns empty list`() {
        val calendarNow = Calendar.getInstance()

        val calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 10)
        calendarStart.set(Calendar.MINUTE, 30)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.HOUR_OF_DAY, 20)
        calendarEnd.set(Calendar.MINUTE, 30)

        val result = EventCreationUtil.isFormValid(
            "title",
            "description",
            "teststreet, 12345 testlocation",
            "12.345",
            "9.876",
            calendarNow,
            "12.12.21",
            "10:30",
            calendarStart,
            "12.12.21",
            "20:30",
            calendarEnd
        )

        assertThat(result).isEmpty()
    }
}