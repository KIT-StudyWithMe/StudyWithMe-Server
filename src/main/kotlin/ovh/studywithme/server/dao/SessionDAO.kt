package ovh.studywithme.server.dao

import ovh.studywithme.server.model.Session
import java.sql.Date

/**
 * A data access object that contains relevant data about a study-group's study-session.
 *
 * @property sessionID The session's unique identifier.
 * @property groupID The unique identifier of the group holding the session.
 * @property place The location where the session will take place.
 * @property startTime The exact time when the session will start.
 * @property duration The session's estimated duration.
 * @constructor Create a new SessionDAO.
 */
data class SessionDAO(
    val sessionID: Long,
    val groupID: Long,
    val place: String,
    val startTime: Long,
    val duration: Int
    ) {
    constructor(session : Session) : this(session.sessionID, session.groupID, session.place, session.startTime, session.duration)

    /**
     * Convert a SessionDAO to a Session.
     *
     * @return corresponding Session
     */
    fun toSession(): Session {
        return Session(this.sessionID, this.groupID, this.place, this.startTime, this.duration)
    }
}