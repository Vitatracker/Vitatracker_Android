package app.mybad.data.db.models

object CourseContract {
    const val TABLE_NAME = "course" //

    object Columns {
        const val ID = "id"
        const val IDN = "idn"
        const val COMMENT = "comment" //
        const val CREATION_DATE = "creation_date" //
        const val UPDATE_DATE = "update_date" //
        const val USER_ID = "user_id" //
        const val USER_IDN = "user_idn" //
        const val REMEDY_ID = "remedy_id" //
        const val START_DATE = "start_date" //
        const val END_DATE = "end_date" //
        const val REMIND_DATE = "remind_date" //
        const val INTERVAL = "interval" //
        const val REGIME = "regime" //
        const val SHOW_USAGE_TIME = "show_usage_time" //
        const val IS_FINISHED = "is_finished" //
        const val IS_INFINITE = "is_infinite" //
        const val NOT_USED = "not_used" //

        const val UPDATED_NETWORK_DATE = "updated_network" // дата последней передачи данных на бек
        const val UPDATED_LOCAL_DATE = "updated_local" // дата последнеого получения данных
    }
}
