package app.mybad.data.db.models

object RemedyContract {
    const val TABLE_NAME = "remedy" //

    object Columns {
        const val ID = "id"
        const val IDN = "idn"

        const val CREATION_DATE = "creation_date" //
        const val UPDATE_DATE = "update_date" //

        const val USER_ID = "user_id" //
        const val USER_IDN = "user_idn" //

        const val NAME = "name" //
        const val DESCRIPTION = "description" //
        const val COMMENT = "comment" //

        const val TYPE = "type" //
        const val ICON = "icon" //
        const val COLOR = "color" //
        const val DOSE = "dose" //
        const val MEASURE_UNIT = "measure_unit" //
        const val PHOTO = "photo" //
        const val BEFORE_FOOD = "before_food" //
        const val NOT_USED = "not_used" //

        const val UPDATED_NETWORK_DATE = "updated_network" // дата последней передачи данных на бек
        const val UPDATED_LOCAL_DATE = "updated_local" // дата последнеого получения данных
        const val DELETED_DATE = "deleted_local" // дата удаления
    }
}
