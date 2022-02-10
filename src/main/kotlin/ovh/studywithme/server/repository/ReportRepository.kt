package ovh.studywithme.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ovh.studywithme.server.model.*

/**
 * Group report repository
 *
 * @constructor Create empty Group report repository
 */
@Repository
interface GroupReportRepository : JpaRepository<StudyGroupReport, Long> {

    /**
     * Exists by reporter i d and group i d and group field
     *
     * @param reporterID
     * @param groupID
     * @param groupField
     * @return
     */
    fun existsByReporterIDAndGroupIDAndGroupField(reporterID:Long, groupID:Long, groupField: StudyGroupField): Boolean

    /**
     * Delete by reporter i d and group i d and group field
     *
     * @param reporterID
     * @param groupID
     * @param groupField
     */
    fun deleteByReporterIDAndGroupIDAndGroupField(reporterID:Long, groupID:Long, groupField: StudyGroupField)
}

/**
 * User report repository
 *
 * @constructor Create empty User report repository
 */
@Repository
interface UserReportRepository : JpaRepository<UserReport, Long> {

    /**
     * Exists by reporter i d and user i d and user field
     *
     * @param reporterID
     * @param userID
     * @param userField
     * @return
     */
    fun existsByReporterIDAndUserIDAndUserField(reporterID:Long, userID:Long, userField: UserField): Boolean

    /**
     * Delete by reporter i d and user i d and user field
     *
     * @param reporterID
     * @param userID
     * @param userField
     * @return
     */
    fun deleteByReporterIDAndUserIDAndUserField(reporterID:Long, userID:Long, userField: UserField): Boolean
}

/**
 * Session report repository
 *
 * @constructor Create empty Session report repository
 */
@Repository
interface SessionReportRepository : JpaRepository<SessionReport, Long> {

    /**
     * Exists by reporter i d and session i d and session field
     *
     * @param reporterID
     * @param sessionID
     * @param sessionField
     * @return
     */
    fun existsByReporterIDAndSessionIDAndSessionField(reporterID:Long, sessionID:Long, sessionField: SessionField): Boolean

    /**
     * Delete by reporter i d and session i d and session field
     *
     * @param reporterID
     * @param sessionID
     * @param sessionField
     * @return
     */
    fun deleteByReporterIDAndSessionIDAndSessionField(reporterID:Long, sessionID:Long, sessionField: SessionField): Boolean
}