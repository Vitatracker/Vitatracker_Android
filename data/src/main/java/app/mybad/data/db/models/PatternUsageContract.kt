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

        const val REMEDY_ID = "remedy_id" // таблетка, нужна для вывода информации
        const val REMEDY_IDN = "remedy_idn" // таблетка, нужна для вывода информации

        const val CREATION_DATE = "creation" //
        const val UPDATED_DATE = "updated" //

        const val USE_TIME = "use_time" //
        const val QUANTITY = "quantity" //

        const val UPDATED_NETWORK_DATE = "updated_network" // дата последней передачи данных на бек
        const val UPDATED_LOCAL_DATE = "updated_local" // дата последнеого получения данных
        const val DELETED_DATE = "deleted_local" // дата удаления
    }
}
