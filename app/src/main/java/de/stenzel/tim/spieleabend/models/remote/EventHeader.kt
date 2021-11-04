package de.stenzel.tim.spieleabend.models.remote

class EventHeader(val title: String) {

    override fun equals(other: Any?): Boolean {

        return if (other is EventHeader) {
            val otherHeader = EventHeader(other.title)
            title == otherHeader.title
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

}