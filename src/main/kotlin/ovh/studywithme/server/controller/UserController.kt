package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.repository.GroupRepository
import ovh.studywithme.server.repository.GroupMemberRepository
import ovh.studywithme.server.repository.UserRepository
import ovh.studywithme.server.repository.UserReportRepository
import java.util.Optional
import org.springframework.stereotype.Service
import ovh.studywithme.server.model.*

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
                         private val groupMemberRepository: GroupMemberRepository) : UserControllerInterface {

    override fun getAllUsers():List<User> {
         return userRepository.findAll()
    }

    override fun getUserByID(userID:Long):User? {
        return userRepository.findById(userID).unwrap()
    }

    override fun getUserLightByID(userID:Long):UserDAO? {
        val user : User? = userRepository.findById(userID).unwrap()
        if (user != null) {
            return UserDAO(user)
        }
        return null
    }

    override fun getUsersGroups(userID:Long): List<StudyGroupMember>? {
        if (!userRepository.existsById(userID)) {
            return null
        }
        //TODO discuss return type
        return groupMemberRepository.findAll().filter { it.userID == userID }
     }

    override fun createUser(user:User): User {
        userRepository.save(user)
        return user
    }

    override fun updateUser(userID: Long, updatedUser : User) : User? {
        if (userID == updatedUser.userID){
            if (userRepository.existsById(updatedUser.userID)) {
                userRepository.save(updatedUser)
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

    override fun getUserByFUID(firebaseUID:String): List<User> {
        return userRepository.findByfirebaseUID(firebaseUID)
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

    override fun getBlockedUsers(): List<User> {
        return userRepository.findAll().filter { it.isBlocked }
    }

    /**
     * Unwrap
     *
     * @param T
     * @return
     */
    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}