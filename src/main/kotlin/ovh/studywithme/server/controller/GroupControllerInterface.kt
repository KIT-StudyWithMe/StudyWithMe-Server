import ovh.studywithme.server.model.StudyGroup
import ovh.studywithme.server.model.User

interface GroupControllerInterface {
    fun getAllGroups(): List<StudyGroup>

    fun createGroup(group:StudyGroup): StudyGroup

    fun getGroupsIndex(start:Int, size:Int): List<StudyGroup>

    fun searchGroup(query:String): List<StudyGroup>

    fun searchGroupByName(name:String): List<StudyGroup>

    fun searchGroupByLecture(lectureName:String): List<StudyGroup>

    fun getGroupByID(groupID:Long): StudyGroup?

    fun getGroupDetails(groupID:Long): StudyGroup

    fun updateGroup(updatedGroup:StudyGroup): StudyGroup?

    fun joinGroup(groupID:Long, userID:Long)

    fun getRequests(groupID:Long): List<User>

    fun changeGroupMemberShip(groupID:Long, userID:Long, isMember:Boolean)

    fun getUsersInGroup(groupID:Long): List<User>

    fun deleteUserFromGroup(groupID:Long, userID:Long)

    fun makeUserAdminInGroup(groupID:Long, userID:Long)
}