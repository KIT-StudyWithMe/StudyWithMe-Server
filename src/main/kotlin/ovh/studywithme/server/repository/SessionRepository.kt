package ovh.studywithme.server.repository

import ovh.studywithme.server.model.SessionAttendee
import ovh.studywithme.server.model.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ovh.studywithme.server.model.SessionField

/**
 * Session repository
 *
 * @constructor Create empty Session repository
 */
@Repository
interface SessionRepository : JpaRepository<Session, Long> {

    /**
     * Find bygroup i d
     *
     * @param groupID
     * @return
     */
    fun findBygroupID(groupID : Long) : List<Session>
}

/**
 * Attendee repository
 *
 * @constructor Create empty Attendee repository
 */
@Repository
interface AttendeeRepository : JpaRepository<SessionAttendee, Long> {

    /**
     * Exists by session i d and user i d
     *
     * @param sessionID
     * @param userID
     * @return
     */
    fun existsBySessionIDAndUserID(sessionID:Long, userID:Long): Boolean

    /**
     * Find by session i d and user i d
     *
     * @param sessionID
     * @param userID
     * @return
     */
    fun findBySessionIDAndUserID(sessionID:Long, userID:Long): SessionAttendee
}