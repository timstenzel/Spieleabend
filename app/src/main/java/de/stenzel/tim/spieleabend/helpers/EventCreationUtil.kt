package de.stenzel.tim.spieleabend.helpers

import java.util.*

object EventCreationUtil {

    fun isFormValid(title: String, description: String, location: String, latitude: String,
                    longitude: String, now: Calendar, startDate: String, startTime: String,
                    start: Calendar, endDate: String, endTime: String, end: Calendar
    ) : List<FormErrors> {
        val errorList = mutableListOf<FormErrors>()

        if (title.isEmpty()) {
            errorList.add(FormErrors.MISSING_TITLE)
        }
        if (description.isEmpty()) {
            errorList.add(FormErrors.MISSING_DESCRIPTION)
        }
        if (location.isEmpty()) {
            errorList.add(FormErrors.MISSING_LOCATION)
        }
        if (latitude.isEmpty()) {
            errorList.add(FormErrors.MISSING_LAT)
        }
        if (longitude.isEmpty()) {
            errorList.add(FormErrors.MISSING_LON)
        }
        if (startDate.isEmpty()) {
            errorList.add(FormErrors.MISSING_START_DATE)
        }
        if (startTime.isEmpty()) {
            errorList.add(FormErrors.MISSING_START_TIME)
        }
        if (endDate.isEmpty()) {
            errorList.add(FormErrors.MISSING_END_DATE)
        }
        if (endTime.isEmpty()) {
            errorList.add(FormErrors.MISSING_END_TIME)
        }
        if (start.timeInMillis < now.timeInMillis) {
            errorList.add(FormErrors.START_IN_PAST)
        }
        if (end.timeInMillis < now.timeInMillis) {
            errorList.add(FormErrors.END_IN_PAST)
        }
        if (start.timeInMillis >= end.timeInMillis) {
            errorList.add(FormErrors.END_BEFORE_START)
        }

        return errorList
    }

    enum class FormErrors {
        MISSING_TITLE,
        MISSING_DESCRIPTION,
        MISSING_LOCATION,
        MISSING_LAT,
        MISSING_LON,
        MISSING_START_DATE,
        MISSING_START_TIME,
        MISSING_END_DATE,
        MISSING_END_TIME,
        START_IN_PAST,
        END_IN_PAST,
        END_BEFORE_START
    }
}