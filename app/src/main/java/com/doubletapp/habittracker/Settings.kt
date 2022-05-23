package com.doubletapp.habittracker

class Settings {
    companion object {
        const val KEY_EDIT_HABIT_ID = "ID_HABIT_TO_EDIT"
        const val ERROR_FIELD_TITLE = "title"
        const val ERROR_FIELD_DESCRIPTION = "description"
        const val ERROR_FIELD_TYPE = "type"
        const val ERROR_FIELD_PRIORITY = "priority"
        const val ERROR_FIELD_COUNT_COMPLETE = "countComplete"
        const val ERROR_FIELD_PERIOD = "period"

        const val LOG_ERROR_HTTP_TAG = "HTTP_ERROR"

        // TODO: to gradle file
        val habitsServiceToken: String = "52f301c6-9492-452d-bc3a-5d354e33a9d0"
            //System.getenv("HABIT_TRACKER_API_TOKEN") ?: ""
    }
}