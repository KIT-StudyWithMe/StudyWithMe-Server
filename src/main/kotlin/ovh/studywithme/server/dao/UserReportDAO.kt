package ovh.studywithme.server.dao

import ovh.studywithme.server.model.UserReport
import ovh.studywithme.server.model.UserField

/**
 * A data access object that contains relevant data about a User-Report.
 *
 * @property userReportID The User-Reports unique identifier.
 * @property reporterID The unique identifier of the user that reports the user-field.
 * @property userID The unique identifier of the user that gets reported.
 * @property userField The field which is reported.
 * @constructor Create a new UserReportDAO.
 */
data class UserReportDAO(
    val userReportID: Long,
    val reporterID: Long,
    val userID: Long,
    val userField: UserField
    ) {
    constructor(userReport : UserReport) : this(userReport.userReportID, userReport.reporterID, userReport.userID, userReport.userField)

    /**
     * Convert a UserReportDAO to a UserReport
     *
     * @return The corresponding UserReport Object
     */
    fun toUserReport(): UserReport {
        return UserReport(this.userReportID, this.reporterID, this.userID, this.userField)
    }
}