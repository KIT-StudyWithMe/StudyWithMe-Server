package ovh.studywithme.server.dao

import ovh.studywithme.server.model.StudyGroupReport
import ovh.studywithme.server.model.StudyGroupField

/**
 * A data access object that contains relevant data about a Group-Report.
 *
 * @property studyGroupReportID The Group-Reports unique identifier.
 * @property reporterID The unique identifier of the user that reports the group-field.
 * @property groupID The unique identifier of the group that gets reported.
 * @property groupField The field which is reported.
 * @constructor Create a new StudyGroupReportDAO.
 */
data class StudyGroupReportDAO(
    val studyGroupReportID: Long,
    val reporterID: Long,
    val groupID: Long,
    val groupField: StudyGroupField
    ) {
    constructor(studyGroupReport : StudyGroupReport) : this(studyGroupReport.studyGroupReportID, studyGroupReport.reporterID, studyGroupReport.groupID, studyGroupReport.groupField)

    /**
     * Convert a StudyGroupReportDAO to a StudyGroupReport
     *
     * @return The corresponding StudyGroupReport Object
     */
    fun toStudyGroupReport(): StudyGroupReport {
        return StudyGroupReport(this.studyGroupReportID, this.reporterID, this.groupID, this.groupField)
    }
}