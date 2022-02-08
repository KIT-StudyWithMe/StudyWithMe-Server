package ovh.studywithme.server.controller

import ovh.studywithme.server.model.User

interface UserControllerInterface {
    fun getAllUsers():List<User>

    fun getUserByID(userID:Long):User?

    fun createUser(user:User): User

    fun updateUser(updatedUser:User): User?

    fun deleteUser(userID:Long): Boolean

    fun getUserByFUID(firebaseUID:String): List<User>
}