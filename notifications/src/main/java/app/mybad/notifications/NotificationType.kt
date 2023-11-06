package app.mybad.notifications

enum class NotificationType {
    PATTERN, // usages на сегодня
    COURSE, // course with remindDate
    MEDICAL_CONTROL, // контроль приема всех препаратов, при последнем припарате
    RESCHEDULE, // пересоздание оповещений на сегодняшний день, выставляется на завтра
}
