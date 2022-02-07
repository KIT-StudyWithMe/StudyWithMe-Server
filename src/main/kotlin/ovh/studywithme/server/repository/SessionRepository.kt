package ovh.studywithme.server.repository

import ovh.studywithme.server.model.SessionAttendee
import ovh.studywithme.server.model.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionRepository : JpaRepository<Session, Long> {

    fun findBygroupID(groupID : Long) : List<Session>
}

@Repository
interface AttendeeRepository : JpaRepository<SessionAttendee, Long>