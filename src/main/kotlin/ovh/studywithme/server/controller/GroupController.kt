package ovh.studywithme.server.controller

import org.springframework.stereotype.Service
import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.dao.StudyGroupDAO
import ovh.studywithme.server.dao.StudyGroupMemberDAO
import ovh.studywithme.server.dao.LectureDAO
import ovh.studywithme.server.model.*
import ovh.studywithme.server.repository.*
import java.util.Optional
import kotlin.collections.ArrayList

/**
 * Implementation of the group controller interface.
 *
 * @property groupRepository
 * @property groupMemberRepository
 * @property userRepository
 * @property groupReportRepository
 * @property informationController
 * @constructor Create a group controller, all variables are instanced by Spring's autowire
 */
@Service
class GroupController(private val groupRepository: GroupRepository,
                      private val groupMemberRepository: GroupMemberRepository,
                      private val groupReportRepository: GroupReportRepository,
                      private val userRepository: UserRepository,
                      private val lectureRepository: LectureRepository,
                      private val informationController: InformationController) : GroupControllerInterface {

    private fun getMemberCount(groupID: Long): Int {
        return groupMemberRepository.findByGroupID(groupID).filter { it.isMember }.size
    }

    override fun getAllGroups(): List<StudyGroupDAO> {
        return groupRepository.findAll().map{StudyGroupDAO(it, getMemberCount(it.groupID))}
    }

    override fun createGroup(group: StudyGroupDAO, userID: Long): StudyGroupDAO {
        val createdGroup : StudyGroup = groupRepository.save(StudyGroup(0, group.name, group.description, group.lectureID, group.sessionFrequency, 
            group.sessionType, group.lectureChapter, group.exercise, false))
        groupMemberRepository.save(StudyGroupMember(0, createdGroup.groupID, userID, true, true))
        return StudyGroupDAO(createdGroup, getMemberCount(createdGroup.groupID))
    }

    override fun searchGroup(query: String): List<StudyGroupDAO> {
        val result : MutableList<StudyGroupDAO> = ArrayList(searchGroupByName(query))
        result.addAll(searchGroupByLecture(query))
        return result.distinctBy { it.groupID }
    }

    override fun searchGroupByName(name: String): List<StudyGroupDAO> {
        return groupRepository.findByNameStartsWith(name).filter { !it.hidden }.map{StudyGroupDAO(it, getMemberCount(it.groupID))}
    }

    override fun searchGroupByLecture(lectureName: String): List<StudyGroupDAO> {
        val allLectures : List<LectureDAO> = informationController.getLecturesByName(0L, lectureName)
        val allGroups : MutableList<StudyGroup> = ArrayList()
        for (currentLecture in allLectures) {
            allGroups.addAll(groupRepository.findByLectureID(currentLecture.lectureID))
        }
        return allGroups.filter { !it.hidden }.map{StudyGroupDAO(it, getMemberCount(it.groupID))}
    }

    override fun getGroupByID(groupID: Long): StudyGroupDAO? {
        val studyGroup: StudyGroup? = groupRepository.findById(groupID).unwrap()
        if (studyGroup != null) {
            return StudyGroupDAO(studyGroup, getMemberCount(studyGroup.groupID))
        }
        return null
    }

    override fun updateGroup(updatedGroup: StudyGroupDAO, groupID: Long): StudyGroupDAO? {
        if (groupRepository.existsById(updatedGroup.groupID) && updatedGroup.groupID==groupID) {
            if (lectureRepository.existsById(updatedGroup.lectureID)){
                groupRepository.save(updatedGroup.toStudyGroup(groupRepository.getById(updatedGroup.groupID).hidden))
                return StudyGroupDAO(groupRepository.getById(updatedGroup.groupID),getMemberCount(updatedGroup.groupID))
            }
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

    override fun toggleGroupMembership(groupID: Long, userID: Long, isMember: Boolean): StudyGroupMemberDAO? {
        if (!groupMemberRepository.existsByGroupIDAndUserID(groupID, userID)) return null
        if (!userRepository.existsById(userID)) return null
        val groupMember : StudyGroupMember = groupMemberRepository.findByGroupIDAndUserID(groupID, userID)
        if (isMember) {
            groupMember.isMember = true
            return StudyGroupMemberDAO(groupMemberRepository.save(groupMember), userRepository.getById(userID).name)
        }
        else {
            // Means user-request to join the group was declined, delete the user's membership in the group
            groupMemberRepository.deleteByGroupIDAndUserID(groupID, userID)
            return StudyGroupMemberDAO(0,0,"deleted",false)
        }
    }

    override fun getUsersInGroup(groupID: Long): List<StudyGroupMemberDAO>? {
        if(!groupRepository.existsById(groupID)) return null
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

    override fun getGroupSuggestions(userID:Long): List<StudyGroupDAO>? {
        val user : User? = userRepository.findById(userID).unwrap()
        if (user==null) return null
        val majorID : Long = user.majorID
        val lectures : List<Lecture> = lectureRepository.findByMajorID(majorID)
        val results : MutableList<StudyGroup> = mutableListOf<StudyGroup>()
        //TODO do that much more efficiently with a join request
        for (lecture in lectures) {
            results.addAll(groupRepository.findByLectureID(lecture.lectureID))
        }
        return results.toList().map{StudyGroupDAO(it, getMemberCount(it.groupID))}
    }

    override fun reportGroupField(groupID:Long, reporterID:Long, field: StudyGroupField): Boolean {
        if (groupRepository.existsById(groupID) && userRepository.existsById(reporterID)) {
            groupReportRepository.save(StudyGroupReport(0, reporterID, groupID, field))
            return true
        }
        return false
    }

    override fun toggleHiddenStatus(groupID: Long): Boolean {
        if (groupRepository.existsById(groupID)) {
            val group = groupRepository.findById(groupID).get()
            group.hidden = !group.hidden
            groupRepository.save(group)
            return true
        }
        return false
    }

    override fun getHiddenStatus(groupID: Long): Boolean {
        if (groupRepository.existsById(groupID)) {
            return groupRepository.findById(groupID).get().hidden
        }
        throw IllegalArgumentException()
    }

    /**
     * Convert Optional Datatype to a Nullable Datatype
     *
     * @param T
     * @return
     */
    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}