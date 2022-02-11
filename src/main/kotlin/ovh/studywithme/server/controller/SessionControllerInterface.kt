package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.SessionDAO
import ovh.studywithme.server.model.SessionField

/**
 * Session controller interface that contains all methods needed for session-management.
 */
interface SessionControllerInterface {
    /**
     * Get all sessions a study-group has planned.
     *
     * @param groupID The group's unique identifier.
     * @return A list containing all planned sessions.
     */
    fun getAllGroupSessions(groupID:Long):List<SessionDAO>

    /**
     * Retrieves a certain study-session. If the session can't be found, null will be returned.
     *
     * @param sessionID The study-session's unique identifier.
     * @return The session that was requested if found, null otherwise.
     */
    fun getSessionByID(sessionID:Long):SessionDAO?

    /**
     * Create a new study-session.
     * The method returns the newly created session with the generated sessionID.
     *
     * @param session session information with which the session should be created
     * @return The study-session that was created.
     */
    fun createSession(session:SessionDAO): SessionDAO

    /**
     * Update a session with the information provided.
     * If there is no session with the corresponding sessionID then null is returned.
     * On success, the updated session will be returned.
     *
     * @param updatedSession
     * @return The updated session with the new content
     */
    fun updateSession(updatedSession:SessionDAO): SessionDAO?

    /**
     * Delete a session that is identified by its id.
     * Verifies first if the session that is to be deleted exists.
     *
     * @param sessionID The session's unique identifier that is up for deletion.
     * @return A boolean which is true if the session was found and deleted, false otherwise.
     */
    fun deleteSession(sessionID:Long): Boolean

    /**
     * Set the user's decision whether to participate in a group's planned session or not.
     * User must be member in the group that planned the session.
     *
     * @param sessionID The session's unique identifier.
     * @param userID The user's unique identifier.
     * @param participates A boolean which is true if the user plans to participate in the session and false if not.
     * @return A boolean which is true if the decision was set successfully and false otherwise.
     */
    fun setParticipation(sessionID:Long, userID:Long, participates:Boolean): Boolean

    /**
     * Report a certain field of a session's details which might contain inappropriate free text.
     *
     * @param sessionID The id of the session whose details contain inappropriate free text.
     * @param reporterID The reporting user's id.
     * @param field The descriptor of the field that's affected.
     * @return A boolean which is true if the data was valid and the report was saved successfully and false otherwise.
     */
    fun reportSessionField(sessionID:Long, reporterID:Long, field: SessionField): Boolean
}