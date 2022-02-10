package ovh.studywithme.server.controller

import ovh.studywithme.server.model.User
import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.model.StudyGroupMember
import ovh.studywithme.server.model.UserField

interface UserControllerInterface {
    fun getAllUsers():List<User>

    fun getUserByID(userID:Long):User?

    fun getUserLightByID(userID:Long):UserDAO?

    fun getUsersGroups(userID:Long): List<StudyGroupMember>?

    fun createUser(user:User): User

    fun updateUser(userID: Long, updatedUser:User): User?

    fun deleteUser(userID:Long): Boolean

    fun getUserByFUID(firebaseUID:String): List<User>

    fun reportUserField(userID:Long, reporterID:Long, field: UserField): Boolean

    fun blockUser(userID:Long, moderatorID:Long): Boolean

    fun getBlockedUsers(): List<User>
}