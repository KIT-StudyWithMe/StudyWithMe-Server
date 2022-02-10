package ovh.studywithme.server.dao

import ovh.studywithme.server.model.Session
import java.sql.Date

/**
 * Session d a o
 *
 * @property sessionID
 * @property groupID
 * @property place
 * @property startTime
 * @property duration
 * @constructor Create empty Session d a o
 */
data class SessionDAO(
    val sessionID: Long,
    val groupID: Long,
    val place: String,
    val startTime: Date, //todo
    val duration: Int
    ) {
    constructor(session : Session) : this(session.sessionID, session.groupID, session.place, session.startTime, session.duration)

    /**
     * To session
     *
     * @return
     */
    fun toSession(): Session {
        return Session(this.sessionID, this.groupID, this.place, this.startTime, this.duration)
    }
}