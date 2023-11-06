package app.mybad.data.db.models

object NotificationContract {
    const val TABLE_NAME = "notification" //

    object Columns {
        const val ID = "id" //
        const val USER_ID = "user_id" //
        const val IS_ENABLED = "is_enabled" //
        const val TYPE = "type" //
        const val TYPE_ID = "type_id" //
        const val DATE = "date" //
        const val TIME = "time_msec" //
    }
}
