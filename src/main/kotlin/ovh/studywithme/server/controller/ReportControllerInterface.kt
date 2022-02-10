package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.StudyGroupReportDAO
import ovh.studywithme.server.dao.UserReportDAO
import ovh.studywithme.server.dao.SessionReportDAO
import ovh.studywithme.server.model.StudyGroupField
import ovh.studywithme.server.model.UserField
import ovh.studywithme.server.model.SessionField

/**
 * Report controller interface
 *
 * @constructor Create empty Report controller interface
 */
interface ReportControllerInterface {

    /**
     * Get all group reports
     *
     * @return
     */
    fun getAllGroupReports(): List<StudyGroupReportDAO>

    /**
     * Get all user reports
     *
     * @return
     */
    fun getAllUserReports(): List<UserReportDAO>

    /**
     * Get all session reports
     *
     * @return
     */
    fun getAllSessionReports(): List<SessionReportDAO>

    /**
     * Delete group report
     *
     * @param reporterID
     * @param groupID
     * @param field
     * @return
     */
    fun deleteGroupReport(reporterID:Long, groupID:Long, field:StudyGroupField): Boolean

    /**
     * Delete user report
     *
     * @param reporterID
     * @param userID
     * @param field
     * @return
     */
    fun deleteUserReport(reporterID:Long, userID:Long, field:UserField): Boolean

    /**
     * Delete session report
     *
     * @param reporterID
     * @param sessionID
     * @param field
     * @return
     */
    fun deleteSessionReport(reporterID:Long, sessionID:Long, field:SessionField): Boolean
}
