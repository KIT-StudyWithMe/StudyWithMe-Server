package ovh.studywithme.server.dao

import ovh.studywithme.server.model.Session
import java.sql.Date

data class SessionDAO(
    val sessionID: Long,
    val groupID: Long,
    val place: String,
    val startTime: Date, //todo
    val duration: Int
    ) {
    constructor(session : Session) : this(session.sessionID, session.groupID, session.place, session.startTime, session.duration)
}