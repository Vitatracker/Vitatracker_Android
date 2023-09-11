package app.mybad.data.db.models

object PatternUsageContract {
    const val TABLE_NAME = "usage_pattern" //

    object Columns {
        const val ID = "id" // local id
        const val IDN = "idn" // network id

        const val USER_ID = "user_id" //
        const val USER_IDN = "user_idn" //

        const val COURSE_ID = "course_id" // курс
        const val COURSE_IDN = "course_idn" // курс

        const val TIME_MINUTES = "time_minutes" //
        const val QUANTITY = "quantity" //

        const val IS_FINISHED = "is_finished" // курс закончен и не нужно брать в выборку

        const val CREATION_DATE = "creation" //
        const val UPDATED_DATE = "updated" //

        const val UPDATED_NETWORK_DATE = "updated_network" // дата последней передачи данных на бек
        const val DELETED_DATE = "deleted_local" // дата удаления
    }
}
