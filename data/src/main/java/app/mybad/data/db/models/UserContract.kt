package app.mybad.data.db.models

object UserContract {
    const val TABLE_NAME = "user" // сопоставление email и user local id

    object Columns {
        const val ID = "id" // local id
        const val IDN = "idn" // network id
        const val NAME = "name" //
        const val EMAIL = "email" //
        const val AVATAR = "avatar" //
        const val CREATION_DATE = "creation" //
        const val UPDATED_DATE = "updated" //
        const val PASSWORD = "password" //
        const val NOT_USED = "not_used" //

        const val TOKEN = "token" //
        const val TOKEN_DATE = "token_date" //
        const val TOKEN_REFRESH = "token_refresh" //
        const val TOKEN_REFRESH_DATE = "token_refresh_date" //

        const val NOTIFICATION_DATE = "notification_date" //

        const val UPDATED_NETWORK_DATE = "updated_network" // дата последней передачи данных на бек
        const val DELETED_DATE = "deleted_local" // дата удаления

        const val IS_DARK_THEME = "is_dark_theme" // темная тема
        const val SYNCHRONIZE_DATE = "synchronize_date" // дата синхронизации с сервером
    }
}
