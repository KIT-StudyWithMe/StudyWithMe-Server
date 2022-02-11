package ovh.studywithme.server.dao

import ovh.studywithme.server.model.SessionReport
import ovh.studywithme.server.model.SessionField

/**
 * A data access object that contains relevant data about a session-report.
 *
 * @property sessionReportID The session-reports unique identifier.
 * @property sessionID The unique identifier of the session that gets reported.
 * @property reporterID The unique identifier of the user that reports the session-field.
 * @property sessionField The field which is reported.
 * @constructor Create a new SessionReportDAO.
 */
data class SessionReportDAO(
    val sessionReportID: Long,
    val sessionID: Long,
    val reporterID: Long,
    val sessionField: SessionField
    ) {
    constructor(sessionReport : SessionReport) : this(sessionReport.sessionReportID, sessionReport.sessionID, sessionReport.reporterID, sessionReport.sessionField)

    /**
     * Convert a SessionReportDAO to a SessionReport
     *
     * @return The corresponding SessionReport object
     */
    fun toSessionReport(): SessionReport {
        return SessionReport(this.sessionReportID, this.sessionID, this.reporterID, this.sessionField)
    }
}