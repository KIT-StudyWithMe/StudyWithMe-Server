package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.SessionDAO
import ovh.studywithme.server.model.Session
import ovh.studywithme.server.model.SessionField

interface SessionControllerInterface {
    fun getAllGroupSessions(groupID:Long):List<SessionDAO>

    fun getSessionByID(sessionID:Long):Session?

    fun createSession(session:SessionDAO): SessionDAO

    fun updateSession(updatedSession:Session): Session?

    fun deleteSession(sessionID:Long): Boolean

    fun setParticipation(sessionID:Long, userID:Long, participates:Boolean): Boolean

    fun reportSessionField(sessionID:Long, reporterID:Long, field: SessionField): Boolean
}