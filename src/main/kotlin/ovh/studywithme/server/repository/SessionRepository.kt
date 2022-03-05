package ovh.studywithme.server.repository

import ovh.studywithme.server.model.SessionAttendee
import ovh.studywithme.server.model.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ovh.studywithme.server.model.SessionField

/**
 * This session repository is used to access the session data in the database in a generic way.
 * Many functions are inherited from the JpaRepository. Even the functions declared below are automatically implemented.
 *
 * @constructor Create a session repository.
 */
@Repository
interface SessionRepository : JpaRepository<Session, Long> {

    /**
     * Finds and returns a list of all study-session a group has planned.
     *
     * @param groupID The study-group's unique identifier.
     * @return A complete list of all study-session the group has planned.
     */
    fun findBygroupID(groupID : Long) : List<Session>
}

/**
 * This attendee repository is used to access the session attendees data in the database in a generic way.
 * Many functions are inherited from the JpaRepository. Even the functions declared below are automatically implemented.
 * A session attendee is someone that either confirmed or declined his participation in a group's planned study-session.
 * The repository is used to store the information if group members will join a study-session or not.
 *
 * @constructor Create a session repository.
 */
@Repository
interface AttendeeRepository : JpaRepository<SessionAttendee, Long> {

    /**
     * Checks is a certain user has made a decision to participate in a group's upcoming study-session or not.
     *
     * @param sessionID The study-session's unique identifier.
     * @param userID The user's unique identifier.
     * @return A boolean which is true if the user has made a decision regarding his participation in the group-session and false otherwise.
     */
    fun existsBySessionIDAndUserID(sessionID:Long, userID:Long): Boolean

    /**
     * Finds a session attendee by the session's id and the user's id, which is a unique combination.
     *
     * @param sessionID The study-session's unique identifier.
     * @param userID The user's unique identifier.
     * @return A boolean which is true if the user has made a decision regarding his participation in the group-session and false otherwise.
     */
    fun findBySessionIDAndUserID(sessionID:Long, userID:Long): SessionAttendee

    /**
     * Finds all group members that made a decision regarding participation in a group session.
     * Returns users that will participate and those that will not participate.
     *
     * @param sessionID The study session's unique identifier.
     * @return A list of all users that decided if they will or will not participate in the session.
     */
    fun findBySessionID(sessionID:Long): List<SessionAttendee>

    /**
     * Deletes all group members that made a decision regarding participation in a given group session.
     *
     * @param sessionID The study session's unique identifier.
     */
    fun deleteBySessionID(sessionID:Long)
}