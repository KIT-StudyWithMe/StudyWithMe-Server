import org.springframework.stereotype.Service
import ovh.studywithme.server.controller.InformationController
import ovh.studywithme.server.model.Lecture
import ovh.studywithme.server.model.StudyGroup
import ovh.studywithme.server.model.StudyGroupMember
import ovh.studywithme.server.model.User
import ovh.studywithme.server.repository.GroupRepository
import ovh.studywithme.server.repository.GroupMemberRepository
import ovh.studywithme.server.repository.UserRepository
import java.util.Optional
import kotlin.collections.ArrayList

@Service
class GroupController(private val groupRepository: GroupRepository,
                      private val groupMemberRepository: GroupMemberRepository,
                      private val userRepository: UserRepository,
                      private val informationController: InformationController) : GroupControllerInterface {

    override fun getAllGroups(): List<StudyGroup> {
        return groupRepository.findAll()
    }

    override fun createGroup(group: StudyGroup): StudyGroup {
        groupRepository.save(group)
        return group
    }

    override fun getGroupsIndex(start: Int, size: Int): List<StudyGroup> {
        //TODO
    }

    override fun searchGroup(query: String): List<StudyGroup> {
        val result : MutableList<StudyGroup> = ArrayList(searchGroupByName(query))
        result.addAll(searchGroupByLecture(query))
        return result
    }

    override fun searchGroupByName(name: String): List<StudyGroup> {
        return groupRepository.findByName(name)
    }

    override fun searchGroupByLecture(lectureName: String): List<StudyGroup> {
        val allLectures : List<Lecture> = informationController.getLecturesByName(0, lectureName)
        val allGroups : MutableList<StudyGroup> = ArrayList()
        for (currentLecture in allLectures) {
            allGroups.addAll(groupRepository.findByLectureID(currentLecture.lectureID))
        }
        return allGroups
    }

    override fun getGroupByID(groupID: Long): StudyGroup? {
        return groupRepository.findById(groupID).unwrap()
    }

    override fun getGroupDetails(groupID: Long): StudyGroup {
        //TODO
    }

    override fun updateGroup(updatedGroup: StudyGroup): StudyGroup? {
        if (groupRepository.existsById(updatedGroup.groupID)) {
            groupRepository.save(updatedGroup)
            return updatedGroup
        }
        return null
    }

    override fun joinGroup(groupID: Long, userID: Long) {
        val newMember : StudyGroupMember = groupMemberRepository.findByGroupIDAndUserID(groupID, userID)
        newMember.isMember = true
        groupMemberRepository.save(newMember)
     }

    override fun getRequests(groupID: Long): List<User> {
        val allRequestMembers : List<StudyGroupMember> = groupMemberRepository.findByGroupID(groupID).filter { !it.isMember }
        val allRequestUsers : MutableList<User> = ArrayList()
        for (currentMember in allRequestMembers) {
            if (userRepository.existsById(currentMember.userID)) {
                allRequestUsers.add(userRepository.findById(currentMember.userID).get())
            }
        }
        return allRequestUsers
    }

    override fun changeGroupMemberShip(groupID: Long, userID: Long, isMember: Boolean) {
        val groupMember : StudyGroupMember = groupMemberRepository.findByGroupIDAndUserID(groupID, userID)
        if (isMember) {
            groupMember.isMember = true
            groupMemberRepository.save(groupMember)
        }
        else {
            // Means user-request to join the group was declined, delete the user's membership in the group
            groupMemberRepository.deleteByGroupIDAndUserID(groupID, userID)
        }
    }

    override fun getUsersInGroup(groupID: Long): List<User> {
        val allGroupMembers : List<StudyGroupMember> = groupMemberRepository.findByGroupID(groupID).filter { it.isMember }
        val allGroupUsers : MutableList<User> = ArrayList()
        for (currentMember in allGroupMembers) {
            if (userRepository.existsById(currentMember.userID)) {
                allGroupUsers.add(userRepository.findById(currentMember.userID).get())
            }
        }
        return allGroupUsers
    }

    override fun deleteUserFromGroup(groupID: Long, userID: Long) {
        groupMemberRepository.deleteByGroupIDAndUserID(groupID, userID)
    }

    override fun makeUserAdminInGroup(groupID: Long, userID: Long) {
        val groupMember : StudyGroupMember = groupMemberRepository.findByGroupIDAndUserID(groupID, userID)
        if (groupMember.isMember) {
            groupMember.isAdmin = true
        }
        groupMemberRepository.save(groupMember)
    }

    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}