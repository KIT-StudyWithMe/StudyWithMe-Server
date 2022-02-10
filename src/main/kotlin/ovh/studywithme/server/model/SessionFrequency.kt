package ovh.studywithme.server.model

/**
 * The projected frequency in which a study group will hold study-sessions.
 */
enum class SessionFrequency {
    /**
     * Only a single study-session is planned. Useful for example for exam-preparation.
     */
    ONCE,

    /**
     * The study-group plans to meet once a week for study sessions.
     */
    WEEKLY,

    /**
     * The study-group plans to meet every two weeks for study sessions.
     */
    TWOWEEKLY,

    /**
     * The study-group plans to meet every three weeks for study sessions.
     */
    THREEWEEKLY,

    /**
     * The study-group plans to meet once a month for study sessions.
     */
    MONTHLY
}