package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.dao.StudyGroupDAO
import ovh.studywithme.server.dao.StudyGroupMemberDAO
import ovh.studywithme.server.model.StudyGroupField

/**
 * Group controller interface that contains all methods needed for group-management.
 */
interface GroupControllerInterface {

    /**
     * Get all study-groups. Returns an empty list if there are no study-groups.
     *
     * @return A list containing all groups.
     */
    fun getAllGroups(): List<StudyGroupDAO>

    /**
     * Create a study-group. The creating user must be made member and administrator in the group he created.
     * Returns the newly created group including the generated groupID.
     *
     * @param group A data access object containing the required information to create a group.
     * @return The newly created group including its generated unique groupID.
     */
    fun createGroup(group:StudyGroupDAO, userID: Long): StudyGroupDAO

    /**
     * Get a certain amount of study-groups starting from an index. Useful to reduce traffic and increase performance
     * when delivering results for searches as not every group in the database is needed.
     *
     * @param start The start index from which to pick the groups.
     * @param size The number of groups that is required from the start index on.
     * @return A list of groups.
     */
    fun getGroupsIndex(start:Int, size:Int): List<StudyGroupDAO>

    /**
     * Search for a group. The query can either be a group's name or a lecture's name a group was created for.
     *
     * @param query Either a group's name or a lecture's name a group was created for.
     * @return A list of groups meeting the search-query.
     */
    fun searchGroup(query:String): List<StudyGroupDAO>

    /**
     * Get a group by its name.
     *
     * @param name The group's name the user is looking for.
     * @return A list of groups whose names match the name the user is looking for.
     */
    fun searchGroupByName(name:String): List<StudyGroupDAO>

    /**
     * Get a group by the lecture's name it was created for.
     *
     * @param lectureName The lecture's name.
     * @return A list of all groups that were created for the lecture the user is looking for.
     */
    fun searchGroupByLecture(lectureName:String): List<StudyGroupDAO>

    /**
     * Get a group by its id. If no group for the given id was found, null will be returned.
     *
     * @param groupID The id for the group that is being requested.
     * @return The group if found, null otherwise.
     */
    fun getGroupByID(groupID:Long): StudyGroupDAO?

    /**
     * Update group with information provided.
     * If there is no group with the groupID in the database, null is returned.
     * On success the updated group is returned.
     *
     * @param updatedGroup The group with the updated information.
     * @return The updated group including the updated information.
     */
    fun updateGroup(updatedGroup:StudyGroupDAO): StudyGroupDAO?

    /**
     * A user requests membership in a study-group. Verifies if both group and user already exist.
     *
     * @param groupID The group's id number.
     * @param userID the user's id number.
     * @return A boolean which is true if the application to the group was successfully stored and false otherwise.
     */
    fun joinGroupRequest(groupID:Long, userID:Long): Boolean

    /**
     * Retrieves a list of all users that applied to the group and have their group-membership pending.
     *
     * @param groupID The group's unique identifier.
     * @return A list users with pending group-membership.
     */
    fun getGroupRequests(groupID: Long): List<UserDAO>

    /**
     * Toggles a user's group-membership to decide about a user's application to a study-group.
     *
     * @param groupID The unique identifier for the group the user applied to.
     * @param userID The applicant's unique identifier.
     * @param isMember A boolean which is true if the user will be accepted and false otherwise.
     * @return A boolean which is true if the user's application was found and therefore can be processed and false otherwise.
     */
    fun toggleGroupMembership(groupID:Long, userID:Long, isMember:Boolean): Boolean

    /**
     * Gets a complete list of all users in a certain study-group.
     *
     * @param groupID The group's unique identifier.
     * @return A list of all group members.
     */
    fun getUsersInGroup(groupID:Long): List<StudyGroupMemberDAO>

    /**
     * Remove a group-member from a study-group. Checks if the user is a group member first.
     *
     * @param groupID The group's unique identifier.
     * @param userID The group-member's unique identifier.
     * @return A boolean which is true if the user was a group-member and then was removed and false otherwise.
     */
    fun deleteUserFromGroup(groupID:Long, userID:Long): Boolean

    /**
     * Deletes a study-group. Only existing groups can be deleted.
     *
     * @param groupID The group's unique identifier.
     * @return A boolean which is true if the group was found and then was deleted and false otherwise.
     */
    fun deleteGroup(groupID: Long): Boolean

    /**
     * Grant admin-status to a group-member. Only admins or moderators should be able to promote group-members.
     * Users with pending member-status must not be promoted.
     *
     * @param groupID The study-groups unique identifier.
     * @param userID The user's id who shall be promoted.
     * @return A boolean which is true if the promotion was successful and false otherwise.
     */
    fun makeUserAdminInGroup(groupID:Long, userID:Long): Boolean

    /**
     * Get Group Suggestions for a User based on his major
     *
     * @param userID The id of the user which groups should be suggested
     * @return A list of Groups for the Major of the User, Null if the User doesnt exist
     */
    fun getGroupSuggestions(userID:Long): List<StudyGroupDAO>?

    /**
     * Report a certain field of a group's details which might contain inappropriate free text.
     *
     * @param groupID The id of the group whose details contain inappropriate free text.
     * @param reporterID The reporting user's id.
     * @param field The descriptor of the field that's affected.
     * @return A boolean which is true if the data was valid and the report was saved successfully and false otherwise.
     */
    fun reportGroupField(groupID:Long, reporterID:Long, field:StudyGroupField): Boolean

    /**
     * Hide group
     *
     * @param groupID
     * @param hidden
     * @return
     */
    fun hideGroup(groupID:Long, hidden:Boolean): Boolean
}