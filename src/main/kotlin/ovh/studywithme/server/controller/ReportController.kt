package ovh.studywithme.server.controller

import org.springframework.stereotype.Service
import ovh.studywithme.server.model.*
import ovh.studywithme.server.repository.GroupReportRepository
import ovh.studywithme.server.repository.UserReportRepository
import ovh.studywithme.server.repository.SessionReportRepository

/**
 * Report controller
 *
 * @property groupReportRepository
 * @property userReportRepository
 * @property sessionReportRepository
 * @constructor Create empty Report controller
 */
@Service
class ReportController(private val groupReportRepository: GroupReportRepository,
                       private val userReportRepository: UserReportRepository,
                       private val sessionReportRepository: SessionReportRepository) : ReportControllerInterface {

    override fun getAllGroupReports(): List<StudyGroupReport> {
        return groupReportRepository.findAll()
    }

    override fun getAllUserReports(): List<UserReport> {
        return userReportRepository.findAll()
    }

    override fun getAllSessionReports(): List<SessionReport> {
        return sessionReportRepository.findAll()
    }

    override fun deleteGroupReport(reporterID:Long, groupID:Long, field: StudyGroupField): Boolean {
        if (groupReportRepository.existsByReporterIDAndGroupIDAndGroupField(reporterID, groupID, field)) {
            groupReportRepository.deleteByReporterIDAndGroupIDAndGroupField(reporterID, groupID, field)
            return true
        }
        return false
    }

    override fun deleteUserReport(reporterID:Long, userID:Long, field: UserField): Boolean {
        if (userReportRepository.existsByReporterIDAndUserIDAndUserField(reporterID, userID, field)) {
            userReportRepository.deleteByReporterIDAndUserIDAndUserField(reporterID, userID, field)
            return true
        }
        return false
    }

    override fun deleteSessionReport(reporterID:Long, sessionID:Long, field: SessionField): Boolean {
        if (sessionReportRepository.existsByReporterIDAndSessionIDAndSessionField(reporterID, sessionID, field)) {
            sessionReportRepository.deleteByReporterIDAndSessionIDAndSessionField(reporterID, sessionID, field)
            return true
        }
        return false

    }

}