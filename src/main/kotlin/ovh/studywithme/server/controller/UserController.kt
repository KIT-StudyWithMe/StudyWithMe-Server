package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.dao.UserDetailDAO
import ovh.studywithme.server.dao.StudyGroupDAO
import ovh.studywithme.server.repository.GroupRepository
import ovh.studywithme.server.repository.GroupMemberRepository
import ovh.studywithme.server.repository.UserRepository
import ovh.studywithme.server.repository.UserReportRepository
import ovh.studywithme.server.repository.MajorRepository
import ovh.studywithme.server.repository.InstitutionRepository
import java.util.Optional
import org.springframework.stereotype.Service
import ovh.studywithme.server.model.*
import ovh.studywithme.server.controller.UserControllerInterface

/**
 * User controller
 *
 * @property userRepository
 * @property userReportRepository
 * @property groupMemberRepository
 * @constructor Create empty User controller
 */
@Service
    class UserController(private val userRepository: UserRepository,
                         private val userReportRepository: UserReportRepository,
                         private val studyGroupRepository: GroupRepository,
                         private val groupMemberRepository: GroupMemberRepository,
                         private val institutionRepository : InstitutionRepository,
                         private val majorRepository: MajorRepository) : UserControllerInterface {

    override fun getAllUsers():List<UserDAO> {
         return userRepository.findAll().map{UserDAO(it)}
    }

    override fun getUserDetailByID(userID:Long):UserDetailDAO? {
        val user : User? = userRepository.findById(userID).unwrap()
        if (user!=null) {
            return getUserDetailDAO(user)
        } 
        return null
    }

    override fun getUserByID(userID:Long):UserDAO? {
        val user : User? = userRepository.findById(userID).unwrap()
        if (user != null) {
            return UserDAO(user)
        }
        return null
    }

    override fun getUsersGroups(userID:Long): List<StudyGroupDAO>? {
        if (!userRepository.existsById(userID)) {
            return null
        }
        val groupMemberEntries: List<StudyGroupMember> = groupMemberRepository.findAll().filter { it.userID == userID }
        val studyGroups : List<StudyGroup> = groupMemberEntries.map{ studyGroupRepository.findById(it.groupID).get() }
        return studyGroups.map{StudyGroupDAO(it)}
     }

    override fun createUser(userDetailDAO:UserDetailDAO): UserDetailDAO {
        val createdUser : User =  userRepository.save(userDetailDAO.toUser())
        return getUserDetailDAO(createdUser)
    }

    override fun updateUser(userID: Long, updatedUser : UserDetailDAO) : UserDetailDAO? {
        if (userID == updatedUser.userID){
            if (userRepository.existsById(updatedUser.userID)) {
                userRepository.save(updatedUser.toUser())
                return updatedUser
            }
        }
        return null
    }

    override fun deleteUser(userID:Long): Boolean {
        if (userRepository.existsById(userID)) {
            userRepository.deleteById(userID)
            return true
        }
        return false
    }

    override fun getUserByFUID(firebaseUID:String): List<UserDAO> {
        return userRepository.findByfirebaseUID(firebaseUID).map{UserDAO(it)}
    }

    override fun reportUserField(userID:Long, reporterID:Long, field: UserField): Boolean {
        if (userRepository.existsById(userID) && userRepository.existsById(reporterID)) {
            userReportRepository.save(UserReport(0, reporterID, userID, field))
            return true
        }
        return false
    }

    override fun blockUser(userID:Long, moderatorID:Long): Boolean {
        if (userRepository.existsById(userID) && userRepository.existsById(moderatorID)) {
            val moderator = userRepository.findById(moderatorID).get()
            val user = userRepository.findById(userID).get()

            if (!moderator.isModerator || user.isModerator) {
                return false
            }

            user.isModerator = true
            userRepository.save(user)
            return true
        }
        return false
    }

    override fun getBlockedUsers(): List<UserDAO> {
        return userRepository.findAll().filter { it.isBlocked }.map{UserDAO(it)}
    }


    /**
     * Converts a User to a UserDetaliDAO.
     * Also gets the InstitutionName and the MajorName of that User
     *
     * @param User
     * @return UserDetailDAO
     */
    fun getUserDetailDAO(user: User): UserDetailDAO {
        val institution : Institution? = institutionRepository.findById(user.institutionID).unwrap()
        val major : Major? = majorRepository.findById(user.majorID).unwrap()
        val institutionID : Long = institution?.institutionID ?: 0
        val institutionName : String = institution?.name ?: "unknown"
        val majorID : Long = major?.majorID ?: 0
        val majorName : String = major?.name ?: "unknown"
        val userDetailDAO : UserDetailDAO = UserDetailDAO(user.userID, user.name, institutionID, institutionName, majorID, majorName, user.contact, user.firebaseUID, user.isModerator)
        return userDetailDAO
    }

    /**
     * Convert Optional Datatype to a Nullable Datatype
     *
     * @param T
     * @return
     */
    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}