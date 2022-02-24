package ovh.studywithme.server.repository

import ovh.studywithme.server.model.StudyGroup
import ovh.studywithme.server.model.StudyGroupMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * This Group repository is used to access the Group-Data in the Database in a generic way.
 * Many functions are inherited from the JpaRepository. Even the functions declared below are automatically implemented.
 *
 * @constructor Create a group repository
 */
@Repository
interface GroupRepository : JpaRepository<StudyGroup, Long> {

    /**
     * Find a Group in the database by name
     *
     * @param name The name of the Group
     * @return A list of matching Groups
     */
    fun findByName(name:String): List<StudyGroup>

    /**
     * Find all groups in the database whose group name starts with the given name
     *
     * @param name The name of the Group which is being used as prefix
     * @return A list of matching Groups
     */
    fun findByNameStartsWith(name:String): List<StudyGroup>

    /**
     * Find a Group in the database by lecture-ID
     *
     * @param lectureId The unique ID of a Lecture to search for
     * @return A list of Groups that belong to that lecture
     */
    fun findByLectureID(lectureId:Long): List<StudyGroup>
}

/**
 * This Group member repository is used to access the Group-Member-Data in the Database in a generic way.
 * Many functions are inherited from the JpaRepository. Even the functions declared below are automatically implemented.
 * Not all GroupMembers in the repository are participants in the group, some just requested the membership in the group.
 *
 * @constructor Create Group-Member repository
 */
@Repository
@Transactional
interface GroupMemberRepository : JpaRepository<StudyGroupMember, Long> {

    /**
     * Find Users that are related to the Group no matter if they requested the participation or if they are actual participants
     *
     * @param groupID Unique GroupID for which all GroupMembers should be returned
     * @return All GroupMembers that are related to the specified GroupID
     */
    fun findByGroupID(groupID:Long): List<StudyGroupMember>

    /**
     * Delete all GroupMembers in a Group
     *
     * @param groupID GroupID for which all GroupMembers should be deleted
     */
    fun deleteByGroupID(groupID:Long)

    /**
     * Find a unique GroupMember by its GroupID and its UserID
     *
     * @param groupID The GroupMembers GroupID
     * @param userID The GroupMembers UserID
     * @return The specified GroupMember in the Database
     */
    fun findByGroupIDAndUserID(groupID:Long, userID:Long): StudyGroupMember

    /**
     * Check if there is a GroupMember for the specified GroupID and UserID
     *
     * @param groupID GroupID of the GroupMember
     * @param userID UserID of the GroupMember
     * @return True if the specified GroupMember exists
     */
    fun existsByGroupIDAndUserID(groupID:Long, userID:Long): Boolean

    /**
     * Delete a GroupMember. 
     * For a GroupMember that requested the membership this is equal to a refusal of that request.
     * For a GroupMember that is in the Group that means the User gets removed from the Group
     *
     * @param groupID GroupID of the GroupMember
     * @param userID UserID of the GroupMember
     */
    fun deleteByGroupIDAndUserID(groupID:Long, userID:Long)
}