package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.model.StudyGroup
import ovh.studywithme.server.model.StudyGroupField
import ovh.studywithme.server.model.User

interface GroupControllerInterface {

    fun getAllGroups(): List<StudyGroup>

    fun createGroup(group:StudyGroup): StudyGroup

    fun getGroupsIndex(start:Int, size:Int): List<StudyGroup>

    fun searchGroup(query:String): List<StudyGroup>

    fun searchGroupByName(name:String): List<StudyGroup>

    fun searchGroupByLecture(lectureName:String): List<StudyGroup>

    fun getGroupByID(groupID:Long): StudyGroup?

    fun getGroupDetails(groupID:Long): StudyGroup?

    fun updateGroup(updatedGroup:StudyGroup): StudyGroup?

    fun joinGroupRequest(groupID:Long, userID:Long): Boolean

    fun getGroupRequests(groupID: Long): List<UserDAO>

    fun toggleGroupMembership(groupID:Long, userID:Long, isMember:Boolean): Boolean

    fun getUsersInGroup(groupID:Long): List<User>

    fun deleteUserFromGroup(groupID:Long, userID:Long): Boolean

    fun deleteGroup(groupID: Long): Boolean

    fun makeUserAdminInGroup(groupID:Long, userID:Long): Boolean

    fun reportGroupField(groupID:Long, reporterID:Long, field:StudyGroupField): Boolean
}