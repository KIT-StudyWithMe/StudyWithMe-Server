package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.SessionDAO
import ovh.studywithme.server.model.SessionField

/**
 * Session controller interface
 *
 * @constructor Create empty Session controller interface
 */
interface SessionControllerInterface {
    /**
     * Get all group sessions
     *
     * @param groupID
     * @return
     */
    fun getAllGroupSessions(groupID:Long):List<SessionDAO>

    /**
     * Get session by i d
     *
     * @param sessionID
     * @return
     */
    fun getSessionByID(sessionID:Long):SessionDAO?

    /**
     * Create session
     *
     * @param session
     * @return
     */
    fun createSession(session:SessionDAO): SessionDAO

    /**
     * Update session
     *
     * @param updatedSession
     * @return
     */
    fun updateSession(updatedSession:SessionDAO): SessionDAO?

    /**
     * Delete session
     *
     * @param sessionID
     * @return
     */
    fun deleteSession(sessionID:Long): Boolean

    /**
     * Set participation
     *
     * @param sessionID
     * @param userID
     * @param participates
     * @return
     */
    fun setParticipation(sessionID:Long, userID:Long, participates:Boolean): Boolean

    /**
     * Report session field
     *
     * @param sessionID
     * @param reporterID
     * @param field
     * @return
     */
    fun reportSessionField(sessionID:Long, reporterID:Long, field: SessionField): Boolean
}