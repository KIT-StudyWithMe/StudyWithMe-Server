package ovh.studywithme.server.controller

import org.springframework.stereotype.Service
import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.model.*
import ovh.studywithme.server.repository.GroupRepository
import ovh.studywithme.server.repository.GroupMemberRepository
import ovh.studywithme.server.repository.GroupReportRepository
import ovh.studywithme.server.repository.UserRepository
import java.util.Optional
import kotlin.collections.ArrayList

@Service
class GroupController(private val groupRepository: GroupRepository,
                      private val groupMemberRepository: GroupMemberRepository,
                      private val userRepository: UserRepository,
                      private val groupReportRepository: GroupReportRepository,
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
        return ArrayList()
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

    override fun getGroupDetails(groupID: Long): StudyGroup? {
        //TODO
        return null
    }

    override fun updateGroup(updatedGroup: StudyGroup): StudyGroup? {
        if (groupRepository.existsById(updatedGroup.groupID)) {
            groupRepository.save(updatedGroup)
            return updatedGroup
        }
        return null
    }

    override fun joinGroupRequest(groupID: Long, userID: Long): Boolean {
        if (groupRepository.existsById(groupID) && userRepository.existsById(userID)) {
            val newMember = StudyGroupMember(0, groupID, userID, false, false)
            groupMemberRepository.save(newMember)
            return true
        }
        return false
     }

    override fun getGroupRequests(groupID: Long): List<UserDAO> {
        val allRequestMembers : List<StudyGroupMember> = groupMemberRepository.findByGroupID(groupID).filter { !it.isMember }
        val allRequestUserDAOs : MutableList<UserDAO> = ArrayList()
        for (currentMember in allRequestMembers) {
            if (userRepository.existsById(currentMember.userID)) {
                val user : User = userRepository.findById(currentMember.userID).get()
                allRequestUserDAOs.add(UserDAO(user.userID, user.name))
            }
        }
        return allRequestUserDAOs
    }

    override fun toggleGroupMembership(groupID: Long, userID: Long, isMember: Boolean): Boolean {
        if (!groupMemberRepository.existsByGroupIDAndUserID(groupID, userID)) return false
        val groupMember : StudyGroupMember = groupMemberRepository.findByGroupIDAndUserID(groupID, userID)
        if (isMember) {
            groupMember.isMember = true
            groupMemberRepository.save(groupMember)
        }
        else {
            // Means user-request to join the group was declined, delete the user's membership in the group
            groupMemberRepository.deleteByGroupIDAndUserID(groupID, userID)
        }
        return true
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

    override fun deleteUserFromGroup(groupID: Long, userID: Long): Boolean {
        if (groupMemberRepository.existsByGroupIDAndUserID(groupID, userID)) {
            groupMemberRepository.deleteByGroupIDAndUserID(groupID, userID)
            return true
        }
        return false
    }

    override fun deleteGroup(groupID: Long): Boolean {
        if (groupMemberRepository.existsById(groupID)) {
            groupMemberRepository.deleteById(groupID)
            return true
        }
        return false
    }

    override fun makeUserAdminInGroup(groupID: Long, userID: Long): Boolean {
        if (groupMemberRepository.existsByGroupIDAndUserID(groupID, userID)) {
            val groupMember: StudyGroupMember = groupMemberRepository.findByGroupIDAndUserID(groupID, userID)
            if (groupMember.isAdmin || !groupMember.isMember) {
                // user is admin already or not an accepted group member
                return false
            }
            groupMember.isAdmin = true
            groupMemberRepository.save(groupMember)
            return true
        }
        return false
    }

    override fun reportGroupField(groupID:Long, reporterID:Long, field: StudyGroupField): Boolean {
        if (groupRepository.existsById(groupID) && userRepository.existsById(reporterID)) {
            groupReportRepository.save(StudyGroupReport(0, reporterID, groupID, field))
            return true
        }
        return false
    }

    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}