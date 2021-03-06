package ovh.studywithme.server.controller

import ovh.studywithme.server.model.Session
import ovh.studywithme.server.model.SessionAttendee
import ovh.studywithme.server.repository.AttendeeRepository
import ovh.studywithme.server.repository.SessionRepository
import java.util.Optional
import org.springframework.stereotype.Service
import ovh.studywithme.server.dao.SessionAttendeeDAO
import ovh.studywithme.server.dao.SessionDAO
import ovh.studywithme.server.model.SessionField
import ovh.studywithme.server.model.SessionReport
import ovh.studywithme.server.repository.SessionReportRepository
import ovh.studywithme.server.repository.UserRepository
import java.util.Date

/**
 * Implementation of the session controller interface.
 *
 * @property sessionRepository
 * @property sessionReportRepository
 * @property userRepository
 * @property attendeeRepository
 * @constructor Create a session controller, all variables are instanced by Spring's autowire
 */
@Service
    class SessionController(private val sessionRepository:SessionRepository,
                            private val sessionReportRepository: SessionReportRepository,
                            private val userRepository: UserRepository,
                            private val attendeeRepository:AttendeeRepository) : SessionControllerInterface {

    override fun createSession(session:SessionDAO): SessionDAO {
        return SessionDAO(sessionRepository.save(session.toSession()))
    }

    override fun getSessionByID(sessionID:Long): SessionDAO? {
        val session: Session? = sessionRepository.findById(sessionID).unwrap()
        if (session!=null){
            return SessionDAO(session)
        }
        return null
    }

    override fun getAllGroupSessions(groupID:Long): List<SessionDAO> {
        val allSessions = ArrayList(sessionRepository.findBygroupID(groupID).map{SessionDAO(it)})
        // 8 hours in ms
        val offset : Long = 8 * 60 * 60 * 1000
        val now = Date().time
        if (allSessions.isEmpty()) {
            return emptyList()
        }
        for (currentSession in allSessions) {
            if ((currentSession.startTime + offset) < now) {
                // the session ended more than offset ms ago, delete it
                allSessions.remove(currentSession)
                if (allSessions.isEmpty()) {
                    return emptyList()
                }
                attendeeRepository.deleteBySessionID(currentSession.sessionID)
                sessionRepository.deleteById(currentSession.sessionID)
            }
        }
        return allSessions
    }

    override fun updateSession(updatedSession:SessionDAO): SessionDAO? {
        if (sessionRepository.existsById(updatedSession.sessionID)) {
            return SessionDAO(sessionRepository.save(updatedSession.toSession()))
        }
        return null
    }

    override fun deleteSession(sessionID:Long): Boolean {
        if (sessionRepository.existsById(sessionID)) {
            attendeeRepository.deleteBySessionID(sessionID)
            sessionRepository.deleteById(sessionID)
            return true
        }
        return false
    }

    override fun setParticipation(sessionID:Long, userID:Long, participates:Boolean): Boolean {
        if (sessionRepository.existsById(sessionID)) {
            // Check if the Attendee exists already, might have changed his mind.
            if (attendeeRepository.existsBySessionIDAndUserID(sessionID, userID)) {
                val attendee = attendeeRepository.findBySessionIDAndUserID(sessionID, userID)
                attendee.participates = participates
                attendeeRepository.save(attendee)
            }
            else {
                //todo check if the user is a group member
                attendeeRepository.save(SessionAttendee(0, sessionID, userID, participates))
            }
            return true
        }
        return false
    }

    override fun getSessionAttendees(sessionID: Long): List<SessionAttendeeDAO>? {
        if (sessionRepository.existsById(sessionID)) {
            val listAttendees = attendeeRepository.findBySessionID(sessionID).filter { it.participates }
            return listAttendees.toList().map{ SessionAttendeeDAO(it) }
        }
        return null
    }

    override fun reportSessionField(sessionID:Long, reporterID:Long, field: SessionField): Boolean {
        if (sessionRepository.existsById(sessionID) && userRepository.existsById(reporterID)) {
            sessionReportRepository.save(SessionReport(0, sessionID, reporterID, field))
            return true
        }
        return false
    }

    /**
     * Convert Optional Datatype to a Nullable Datatype
     *
     * @param T
     * @return
     */
    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}