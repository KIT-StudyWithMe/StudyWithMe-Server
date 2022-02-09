package ovh.studywithme.server.repository

import ovh.studywithme.server.model.SessionAttendee
import ovh.studywithme.server.model.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ovh.studywithme.server.model.SessionField

@Repository
interface SessionRepository : JpaRepository<Session, Long> {

    fun findBygroupID(groupID : Long) : List<Session>
}

@Repository
interface AttendeeRepository : JpaRepository<SessionAttendee, Long> {

    fun existsBySessionIDAndUserID(sessionID:Long, userID:Long): Boolean

    fun findBySessionIDAndUserID(sessionID:Long, userID:Long): SessionAttendee
}