package ovh.studywithme.server.controller

import org.springframework.stereotype.Service
import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.dao.StudyGroupDAO
import ovh.studywithme.server.dao.StudyGroupMemberDAO
import ovh.studywithme.server.dao.LectureDAO
import ovh.studywithme.server.model.*
import ovh.studywithme.server.repository.GroupRepository
import ovh.studywithme.server.repository.GroupMemberRepository
import ovh.studywithme.server.repository.GroupReportRepository
import ovh.studywithme.server.repository.UserRepository
import java.util.Optional
import kotlin.collections.ArrayList

/**
 * Group controller
 *
 * @property groupRepository
 * @property groupMemberRepository
 * @property userRepository
 * @property groupReportRepository
 * @property informationController
 */
@Service
class GroupController(private val groupRepository: GroupRepository,
                      private val groupMemberRepository: GroupMemberRepository,
                      private val userRepository: UserRepository,
                      private val groupReportRepository: GroupReportRepository,
                      private val informationController: InformationController) : GroupControllerInterface {

    override fun getAllGroups(): List<StudyGroupDAO> {
        return groupRepository.findAll().map{StudyGroupDAO(it)}
    }

    override fun createGroup(group: StudyGroupDAO, userID: Long): StudyGroupDAO {
        val createdGroup : StudyGroup = groupRepository.save(StudyGroup(0, group.name, group.description, group.lectureID, group.sessionFrequency, 
            group.sessionType, group.lectureChapter, group.exercise, false))
        groupMemberRepository.save(StudyGroupMember(0, createdGroup.groupID, userID, true, true))
        return StudyGroupDAO(createdGroup)
    }

    override fun getGroupsIndex(start: Int, size: Int): List<StudyGroupDAO> {
        //TODO
        return ArrayList()
    }

    override fun searchGroup(query: String): List<StudyGroupDAO> {
        val result : MutableList<StudyGroupDAO> = ArrayList(searchGroupByName(query))
        result.addAll(searchGroupByLecture(query))
        return result
    }

    override fun searchGroupByName(name: String): List<StudyGroupDAO> {
        return groupRepository.findByName(name).map{StudyGroupDAO(it)}
    }

    override fun searchGroupByLecture(lectureName: String): List<StudyGroupDAO> {
        val allLectures : List<LectureDAO> = informationController.getLecturesByName(0, lectureName)
        val allGroups : MutableList<StudyGroup> = ArrayList()
        for (currentLecture in allLectures) {
            allGroups.addAll(groupRepository.findByLectureID(currentLecture.lectureID))
        }
        return allGroups.map{StudyGroupDAO(it)}
    }

    override fun getGroupByID(groupID: Long): StudyGroupDAO? {
        val studyGroup: StudyGroup? = groupRepository.findById(groupID).unwrap()
        if (studyGroup != null) {
            return StudyGroupDAO(studyGroup)
        }
        return null
    }

    override fun updateGroup(updatedGroup: StudyGroupDAO): StudyGroupDAO? {
        if (groupRepository.existsById(updatedGroup.groupID)) {
            groupRepository.save(updatedGroup.toStudyGroup(groupRepository.getById(updatedGroup.groupID).hidden))
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
                allRequestUserDAOs.add(UserDAO(userRepository.findById(currentMember.userID).get()))
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

    override fun getUsersInGroup(groupID: Long): List<StudyGroupMemberDAO> {
        val allGroupMembers : List<StudyGroupMember> = groupMemberRepository.findByGroupID(groupID).filter { it.isMember }
        val allGroupUsers : MutableList<StudyGroupMemberDAO> = ArrayList()
        for (currentMember in allGroupMembers) {
            if (userRepository.existsById(currentMember.userID)) {
                allGroupUsers.add(StudyGroupMemberDAO(userRepository.findById(currentMember.userID).get(), groupID, currentMember.isAdmin))
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
        if (groupRepository.existsById(groupID)) {
            groupMemberRepository.deleteByGroupID(groupID)
            groupRepository.deleteById(groupID)
            //TODO delete Lecture if its no longer needed
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

    /**
     * Convert Optional Datatype to a Nullable Datatype
     *
     * @param T
     * @return
     */
    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}