package ovh.studywithme.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ovh.studywithme.server.model.*
/**
 * The following repositories are grouped under the name "report repositories", as they all contain information
 * about freetext fields being reported.
 */


/**
 * This group report repository is used to access the group report data in the database in a generic way.
 * Many functions are inherited from the JpaRepository. Even the functions declared below are automatically implemented.
 *
 * @constructor Create a group report repository.
 */
@Repository
interface GroupReportRepository : JpaRepository<StudyGroupReport, Long> {

    /**
     * Checks if a certain user reported a certain freetext field in a certain group's details.
     *
     * @param reporterID The reporting user's unique identifier.
     * @param groupID The unique identifier for the group that a field was reported for.
     * @param groupField The reported freetext field's descriptor.
     * @return True will be returned if a report for the given parameters was found, false otherwise.
     */
    fun existsByReporterIDAndGroupIDAndGroupField(reporterID:Long, groupID:Long, groupField: StudyGroupField): Boolean

    /**
     * Deletes a report for a group-field. The report can be uniquely identified by the following parameters.
     *
     * @param reporterID The reporting user's unique identifier.
     * @param groupID The unique identifier for the group that a field was reported for.
     * @param groupField The reported freetext field's descriptor.
     */
    fun deleteByReporterIDAndGroupIDAndGroupField(reporterID:Long, groupID:Long, groupField: StudyGroupField)
}

/**
 * This user report repository is used to access the user report data in the database in a generic way.
 * Many functions are inherited from the JpaRepository. Even the functions declared below are automatically implemented.
 *
 * @constructor Create a group report repository.
 */
@Repository
interface UserReportRepository : JpaRepository<UserReport, Long> {

    /**
     * Checks if a certain user reported a certain freetext field in another user's details.
     *
     * @param reporterID The reporting user's unique identifier.
     * @param userID The unique identifier for the reported user.
     * @param userField The reported freetext field's descriptor.
     * @return True will be returned if a report for the given parameters was found, false otherwise.
     */
    fun existsByReporterIDAndUserIDAndUserField(reporterID:Long, userID:Long, userField: UserField): Boolean

    /**
     * Deletes a report for a user-field. The report can be uniquely identified by the following parameters.
     *
     * @param reporterID The reporting user's unique identifier.
     * @param userID The unique identifier for the reported user.
     * @param userField The reported freetext field's descriptor.
     */
    fun deleteByReporterIDAndUserIDAndUserField(reporterID:Long, userID:Long, userField: UserField)
}

/**
 * This session report repository is used to access the session report data in the database in a generic way.
 * Many functions are inherited from the JpaRepository. Even the functions declared below are automatically implemented.
 *
 * @constructor Create a session report repository.
 */
@Repository
interface SessionReportRepository : JpaRepository<SessionReport, Long> {

    /**
     * Checks if a certain user reported a certain freetext field in a session's details.
     *
     * @param reporterID The reporting user's unique identifier.
     * @param sessionID The unique identifier for the session that a field was reported for.
     * @param sessionField The reported freetext field's descriptor.
     * @return True will be returned if a report for the given parameters was found, false otherwise.
     */
    fun existsByReporterIDAndSessionIDAndSessionField(reporterID:Long, sessionID:Long, sessionField: SessionField): Boolean

    /**
     * Delete by reporter i d and session i d and session field
     *
     * @param reporterID The reporting user's unique identifier.
     * @param sessionID The unique identifier for the session that a field was reported for.
     * @param sessionField The reported freetext field's descriptor.
     */
    fun deleteByReporterIDAndSessionIDAndSessionField(reporterID:Long, sessionID:Long, sessionField: SessionField)
}