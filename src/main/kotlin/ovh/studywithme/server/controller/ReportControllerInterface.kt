package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.StudyGroupReportDAO
import ovh.studywithme.server.dao.UserReportDAO
import ovh.studywithme.server.dao.SessionReportDAO
import ovh.studywithme.server.model.StudyGroupField
import ovh.studywithme.server.model.UserField
import ovh.studywithme.server.model.SessionField

/**
 * Report controller interface that contains all methods needed for reports-management.
 */
interface ReportControllerInterface {

    /**
     * Get all reports concerning fields in groups' details.
     *
     * @return A list of all groups that fields have been reported for.
     */
    fun getAllGroupReports(): List<StudyGroupReportDAO>

    /**
     * Get all reports concerning fields in users' details.
     *
     * @return A list of all users that fields have been reported for.
     */
    fun getAllUserReports(): List<UserReportDAO>

    /**
     * Get all reports concerning fields in sessions' details.
     *
     * @return A list of all sessions that fields have been reported for.
     */
    fun getAllSessionReports(): List<SessionReportDAO>

    /**
     * Delete a user's report concerning a group's field in group details.
     *
     * @param reporterID The reporting user's unique identifier.
     * @param groupID The id of the group whose details contain the reported field.
     * @param field The reported field.
     * @return A boolean which is true if the report was found and successfully deleted and false otherwise.
     */
    fun deleteGroupReport(reporterID:Long, groupID:Long, field:StudyGroupField): Boolean

    /**
     * Delete a user's report concerning another user's field in user details.
     *
     * @param reporterID The reporting user's unique identifier.
     * @param userID The id of the user whose details contain the reported field.
     * @param field The reported field.
     * @return A boolean which is true if the report was found and successfully deleted and false otherwise.
     */
    fun deleteUserReport(reporterID:Long, userID:Long, field:UserField): Boolean

    /**
     * Delete a user's report concerning a session's field in session details.
     *
     * @param reporterID The reporting user's unique identifier.
     * @param sessionID The id of the session whose details contain the reported field.
     * @param field The reported field.
     * @return A boolean which is true if the report was found and successfully deleted and false otherwise.
     */
    fun deleteSessionReport(reporterID:Long, sessionID:Long, field:SessionField): Boolean
}
