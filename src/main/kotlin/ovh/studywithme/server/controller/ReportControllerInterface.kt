package ovh.studywithme.server.controller

import ovh.studywithme.server.model.*

interface ReportControllerInterface {

    fun getAllGroupReports(): List<StudyGroupReport>

    fun getAllUserReports(): List<UserReport>

    fun getAllSessionReports(): List<SessionReport>

    //fun getGroupReportDetails(reporterID:Long, groupID:Long, field:StudyGroupField): StudyGroupReport

    //fun getUserReportDetails(reporterID:Long, userID:Long, field:UserField): UserReport

    //fun getSessionReportDetails(reporterID:Long, sessionID:Long, field:SessionField): SessionReport

    fun deleteGroupReport(reporterID:Long, groupID:Long, field:StudyGroupField): Boolean

    fun deleteUserReport(reporterID:Long, userID:Long, field:UserField): Boolean

    fun deleteSessionReport(reporterID:Long, sessionID:Long, field:SessionField): Boolean
}
