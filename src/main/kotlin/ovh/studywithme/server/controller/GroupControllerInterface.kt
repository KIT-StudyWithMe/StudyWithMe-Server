package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.dao.StudyGroupDAO
import ovh.studywithme.server.dao.StudyGroupMemberDAO
import ovh.studywithme.server.model.StudyGroupField

/**
 * Group controller interface
 *
 * @constructor Create empty Group controller interface
 */
interface GroupControllerInterface {

    /**
     * Get all groups
     *
     * @return
     */
    fun getAllGroups(): List<StudyGroupDAO>

    /**
     * Create group
     *
     * @param group
     * @return
     */
    fun createGroup(group:StudyGroupDAO, userID: Long): StudyGroupDAO

    /**
     * Get groups index
     *
     * @param start
     * @param size
     * @return
     */
    fun getGroupsIndex(start:Int, size:Int): List<StudyGroupDAO>

    /**
     * Search group
     *
     * @param query
     * @return
     */
    fun searchGroup(query:String): List<StudyGroupDAO>

    /**
     * Search group by name
     *
     * @param name
     * @return
     */
    fun searchGroupByName(name:String): List<StudyGroupDAO>

    /**
     * Search group by lecture
     *
     * @param lectureName
     * @return
     */
    fun searchGroupByLecture(lectureName:String): List<StudyGroupDAO>

    /**
     * Get group by i d
     *
     * @param groupID
     * @return
     */
    fun getGroupByID(groupID:Long): StudyGroupDAO?

    /**
     * Update group
     *
     * @param updatedGroup
     * @return
     */
    fun updateGroup(updatedGroup:StudyGroupDAO): StudyGroupDAO?

    /**
     * Join group request
     *
     * @param groupID
     * @param userID
     * @return
     */
    fun joinGroupRequest(groupID:Long, userID:Long): Boolean

    /**
     * Get group requests
     *
     * @param groupID
     * @return
     */
    fun getGroupRequests(groupID: Long): List<UserDAO>

    /**
     * Toggle group membership
     *
     * @param groupID
     * @param userID
     * @param isMember
     * @return
     */
    fun toggleGroupMembership(groupID:Long, userID:Long, isMember:Boolean): Boolean

    /**
     * Get users in group
     *
     * @param groupID
     * @return
     */
    fun getUsersInGroup(groupID:Long): List<StudyGroupMemberDAO>

    /**
     * Delete user from group
     *
     * @param groupID
     * @param userID
     * @return
     */
    fun deleteUserFromGroup(groupID:Long, userID:Long): Boolean

    /**
     * Delete group
     *
     * @param groupID
     * @return
     */
    fun deleteGroup(groupID: Long): Boolean

    /**
     * Make user admin in group
     *
     * @param groupID
     * @param userID
     * @return
     */
    fun makeUserAdminInGroup(groupID:Long, userID:Long): Boolean

    /**
     * Report group field
     *
     * @param groupID
     * @param reporterID
     * @param field
     * @return
     */
    fun reportGroupField(groupID:Long, reporterID:Long, field:StudyGroupField): Boolean
}