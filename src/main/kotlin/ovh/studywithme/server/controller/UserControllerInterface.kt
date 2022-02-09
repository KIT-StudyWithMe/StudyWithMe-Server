package ovh.studywithme.server.controller

import ovh.studywithme.server.model.User
import ovh.studywithme.server.dao.UserDAO

interface UserControllerInterface {
    fun getAllUsers():List<User>

    fun getUserByID(userID:Long):User?

    fun getUserLightByID(userID:Long):UserDAO?

    fun createUser(user:User): User

    fun updateUser(userID: Long, updatedUser:User): User?

    fun deleteUser(userID:Long): Boolean

    fun getUserByFUID(firebaseUID:String): List<User>
}