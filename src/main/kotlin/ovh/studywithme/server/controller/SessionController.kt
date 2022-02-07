package ovh.studywithme.server.controller

import ovh.studywithme.server.model.Session
import ovh.studywithme.server.model.SessionAttendee
import ovh.studywithme.server.repository.AttendeeRepository
import ovh.studywithme.server.repository.SessionRepository
import java.util.Optional
import org.springframework.stereotype.Service

@Service
    class SessionController(private val sessionRepository:SessionRepository, private val attendeeRepository:AttendeeRepository) : SessionControllerInterface {

    override fun createSession(session:Session): Session {
        sessionRepository.save(session)
        return session
    }

    override fun getSessionByID(sessionID:Long): Session? {
        return sessionRepository.findById(sessionID).unwrap()
    }

    override fun getAllGroupSessions(groupID:Long): List<Session> {
        return sessionRepository.findBygroupID(groupID)
    }

    override fun updateSession(updatedSession:Session): Session? {
        if (sessionRepository.existsById(updatedSession.sessionID)) {
            sessionRepository.save(updatedSession)
            return updatedSession
        }
        return null
    }

    override fun deleteSession(sessionID:Long): Boolean {
        if (sessionRepository.existsById(sessionID)) {
            sessionRepository.deleteById(sessionID)
            return true
        }
        return false
    }

    override fun setParticipation(sessionID:Long, userID:Long, participates:Boolean): Boolean {
        if (sessionRepository.existsById(sessionID)) {
            attendeeRepository.save(SessionAttendee(0, sessionID, userID, participates))
            return true
        }
        return false
    }

    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}