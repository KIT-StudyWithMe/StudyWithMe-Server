package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.model.StudyGroup
import ovh.studywithme.server.model.StudyGroupField
import ovh.studywithme.server.model.User

/**
 * Controller interface for study groups.
 *
 * Provides all functionality to administrate study groups.
 **/
interface GroupControllerInterface {
    /**
     * Get all existing study group from the database.
     *
     * @return A list including all existing study groups.
     */
    fun getAllGroups(): List<StudyGroup>

    /**
     * Stores a new study group in the appropriate repository.
     *
     * @param group A newly created study group to be stored.
     * @return The saved study group as indicator of a successful operation.
     */
    fun createGroup(group:StudyGroup): StudyGroup

    /**
     * Get groups index
     *
     * @param start
     * @param size
     * @return
     */
    fun getGroupsIndex(start:Int, size:Int): List<StudyGroup>

    /**
     * Search group
     *
     * @param query
     * @return
     */
    fun searchGroup(query:String): List<StudyGroup>

    /**
     * Search group by name
     *
     * @param name
     * @return
     */
    fun searchGroupByName(name:String): List<StudyGroup>

    /**
     * Search group by lecture
     *
     * @param lectureName
     * @return
     */
    fun searchGroupByLecture(lectureName:String): List<StudyGroup>

    /**
     * Get group by i d
     *
     * @param groupID
     * @return
     */
    fun getGroupByID(groupID:Long): StudyGroup?

    /**
     * Get group details
     *
     * @param groupID
     * @return
     */
    fun getGroupDetails(groupID:Long): StudyGroup?

    /**
     * Update group
     *
     * @param updatedGroup
     * @return
     */
    fun updateGroup(updatedGroup:StudyGroup): StudyGroup?

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
    fun getUsersInGroup(groupID:Long): List<User>

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
     * @param userID
     * @param field
     * @return
     */
    fun reportGroupField(groupID:Long, reporterID:Long, field:StudyGroupField): Boolean
}