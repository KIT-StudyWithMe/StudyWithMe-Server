package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.dao.StudyGroupDAO
import ovh.studywithme.server.model.StudyGroupField

interface GroupControllerInterface {

    fun getAllGroups(): List<StudyGroupDAO>

    fun createGroup(group:StudyGroupDAO): StudyGroupDAO

    fun getGroupsIndex(start:Int, size:Int): List<StudyGroupDAO>

    fun searchGroup(query:String): List<StudyGroupDAO>

    fun searchGroupByName(name:String): List<StudyGroupDAO>

    fun searchGroupByLecture(lectureName:String): List<StudyGroupDAO>

    fun getGroupByID(groupID:Long): StudyGroupDAO?

    fun updateGroup(updatedGroup:StudyGroupDAO): StudyGroupDAO?

    fun joinGroupRequest(groupID:Long, userID:Long): Boolean

    fun getGroupRequests(groupID: Long): List<UserDAO>

    fun toggleGroupMembership(groupID:Long, userID:Long, isMember:Boolean): Boolean

    fun getUsersInGroup(groupID:Long): List<UserDAO>

    fun deleteUserFromGroup(groupID:Long, userID:Long): Boolean

    fun deleteGroup(groupID: Long): Boolean

    fun makeUserAdminInGroup(groupID:Long, userID:Long): Boolean

    fun reportGroupField(groupID:Long, reporterID:Long, field:StudyGroupField): Boolean
}