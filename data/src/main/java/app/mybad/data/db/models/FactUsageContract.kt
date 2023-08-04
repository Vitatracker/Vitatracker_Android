package app.mybad.data.db.models

object FactUsageContract {
    const val TABLE_NAME = "usage" //

    object Columns {
        const val ID = "id" // local id
        const val IDN = "idn" // network id

        const val USAGE_ID = "usage_id" //
        const val USAGE_IDN = "usage_idn" //

        const val FACT_USE_TIME = "fact_use_time" //

        const val UPDATED_NETWORK_DATE = "updated_network" // дата последней передачи данных на бек
        const val UPDATED_LOCAL_DATE = "updated_local" // дата последнеого получения данных
    }
}
