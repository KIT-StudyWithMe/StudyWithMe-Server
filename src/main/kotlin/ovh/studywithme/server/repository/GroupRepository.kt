package ovh.studywithme.server.repository

import ovh.studywithme.server.model.StudyGroup
import ovh.studywithme.server.model.StudyGroupMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Group repository
 *
 * @constructor Create empty Group repository
 */
@Repository
interface GroupRepository : JpaRepository<StudyGroup, Long> {

    /**
     * Find by name
     *
     * @param name
     * @return
     */
    fun findByName(name:String): List<StudyGroup>

    /**
     * Find by lecture i d
     *
     * @param lectureId
     * @return
     */
    fun findByLectureID(lectureId:Long): List<StudyGroup>
}

/**
 * Group member repository
 *
 * @constructor Create empty Group member repository
 */
@Repository
interface GroupMemberRepository : JpaRepository<StudyGroupMember, Long> {

    /**
     * Find by group i d
     *
     * @param groupID
     * @return
     */
    fun findByGroupID(groupID:Long): List<StudyGroupMember>

    /**
     * Delete by group i d
     *
     * @param groupID
     */
    fun deleteByGroupID(groupID:Long)

    /**
     * Find by group i d and user i d
     *
     * @param groupID
     * @param userID
     * @return
     */
    fun findByGroupIDAndUserID(groupID:Long, userID:Long): StudyGroupMember

    /**
     * Exists by group i d and user i d
     *
     * @param groupID
     * @param userID
     * @return
     */
    fun existsByGroupIDAndUserID(groupID:Long, userID:Long): Boolean

    /**
     * Delete by group i d and user i d
     *
     * @param groupID
     * @param userID
     */
    fun deleteByGroupIDAndUserID(groupID:Long, userID:Long)
}