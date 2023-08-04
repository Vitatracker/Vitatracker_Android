package app.mybad.data.db.models

object UsageContract {
    const val TABLE_NAME = "usage" //

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

        const val FACT_USE_TIME = "fact_use_time" //
        const val USE_TIME = "use_time" //

        const val QUANTITY = "quantity" //

        const val IS_DELETED = "is_deleted" //
        const val NOT_USED = "not_used" //

        const val UPDATED_NETWORK_DATE = "updated_network" // дата последней передачи данных на бек
        const val UPDATED_LOCAL_DATE = "updated_local" // дата последнеого получения данных
    }
}
