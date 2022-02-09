package ovh.studywithme.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ovh.studywithme.server.model.*

@Repository
interface GroupReportRepository : JpaRepository<StudyGroupReport, Long> {

    fun existsByReporterIDAndGroupIDAndGroupField(reporterID:Long, groupID:Long, groupField: StudyGroupField): Boolean

    fun deleteByReporterIDAndGroupIDAndGroupField(reporterID:Long, groupID:Long, groupField: StudyGroupField)
}

@Repository
interface UserReportRepository : JpaRepository<UserReport, Long> {

    fun existsByReporterIDAndUserIDAndUserField(reporterID:Long, userID:Long, userField: UserField): Boolean

    fun deleteByReporterIDAndUserIDAndUserField(reporterID:Long, userID:Long, userField: UserField): Boolean
}

@Repository
interface SessionReportRepository : JpaRepository<SessionReport, Long> {

    fun existsByReporterIDAndSessionIDAndSessionField(reporterID:Long, sessionID:Long, sessionField: SessionField): Boolean

    fun deleteByReporterIDAndSessionIDAndSessionField(reporterID:Long, sessionID:Long, sessionField: SessionField): Boolean
}