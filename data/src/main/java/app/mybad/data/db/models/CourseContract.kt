package app.mybad.data.db.models

object CourseContract {
    const val TABLE_NAME = "course" //

    object Columns {
        const val ID = "id"
        const val IDN = "idn"

        const val USER_ID = "user_id" //
        const val USER_IDN = "user_idn" //

        const val REMEDY_ID = "remedy_id" //
        const val REMEDY_IDN = "remedy_idn" //

        const val START_DATE = "start_date" //
        const val START_DATE_FUTURE = "start_date_future" //
        const val END_DATE = "end_date" //
        const val END_DATE_FUTURE = "end_date_future" //

        const val REGIME = "regime" //
        const val IS_FINISHED = "is_finished" //
        const val IS_INFINITE = "is_infinite" //
        const val SHOW_USAGE_TIME = "show_usage_time" //
        const val NOT_USED = "not_used" //

        // строка паттерн: [время приема в минутах]-[количество препарата за этот прием]; - разделитель
        const val PATTERN_USAGES = "pattern_usages"

        const val COMMENT = "comment" //

        const val REMIND_DATE = "remind_date" // это дата оповещения о новом курсе, это не старт нового курса
        const val INTERVAL = "interval" // старт нового курса чере интервал после конца курса

        const val CREATION_DATE = "creation_date" //
        const val UPDATE_DATE = "update_date" //

        const val UPDATED_NETWORK_DATE = "updated_network" // дата последней передачи данных на бек
        const val DELETED_DATE = "deleted_local" // дата удаления
    }
}
