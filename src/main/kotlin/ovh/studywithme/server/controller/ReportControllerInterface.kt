package ovh.studywithme.server.controller

import ovh.studywithme.server.model.*

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
    fun getAllGroupReports(): List<StudyGroupReport>

    /**
     * Get all user reports
     *
     * @return
     */
    fun getAllUserReports(): List<UserReport>

    /**
     * Get all session reports
     *
     * @return
     */
    fun getAllSessionReports(): List<SessionReport>

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
