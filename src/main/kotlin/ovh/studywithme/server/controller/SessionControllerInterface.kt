package ovh.studywithme.server.controller

import ovh.studywithme.server.model.Session
import ovh.studywithme.server.model.SessionField

interface SessionControllerInterface {
    fun getAllGroupSessions(groupID:Long):List<Session>

    fun getSessionByID(sessionID:Long):Session?

    fun createSession(session:Session): Session

    fun updateSession(updatedSession:Session): Session?

    fun deleteSession(sessionID:Long): Boolean

    fun setParticipation(sessionID:Long, userID:Long, participates:Boolean): Boolean

    fun reportSessionField(sessionID:Long, reporterID:Long, field: SessionField): Boolean
}