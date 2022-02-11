package ovh.studywithme.server.controller

import org.springframework.stereotype.Service
import ovh.studywithme.server.dao.StudyGroupReportDAO
import ovh.studywithme.server.dao.UserReportDAO
import ovh.studywithme.server.dao.SessionReportDAO
import ovh.studywithme.server.model.StudyGroupField
import ovh.studywithme.server.model.UserField
import ovh.studywithme.server.model.SessionField
import ovh.studywithme.server.repository.GroupReportRepository
import ovh.studywithme.server.repository.UserReportRepository
import ovh.studywithme.server.repository.SessionReportRepository

/**
 * Implementation of the report controller interface.
 *
 * @property groupReportRepository
 * @property userReportRepository
 * @property sessionReportRepository
 * @constructor Create a report controller, all variables are instanced by Spring's autowire
 */
@Service
class ReportController(private val groupReportRepository: GroupReportRepository,
                       private val userReportRepository: UserReportRepository,
                       private val sessionReportRepository: SessionReportRepository) : ReportControllerInterface {

    override fun getAllGroupReports(): List<StudyGroupReportDAO> {
        return groupReportRepository.findAll().map{StudyGroupReportDAO(it)}
    }

    override fun getAllUserReports(): List<UserReportDAO> {
        return userReportRepository.findAll().map{UserReportDAO(it)}
    }

    override fun getAllSessionReports(): List<SessionReportDAO> {
        return sessionReportRepository.findAll().map{SessionReportDAO(it)}
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