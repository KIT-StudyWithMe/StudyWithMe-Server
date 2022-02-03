package ovh.studywithme.server.controller

import ovh.studywithme.server.model.User

interface UserControllerInterface {
    fun getAllUsers():List<User>

    fun getUserByID(userID:Long):User?

    fun createUser(user:User): User

    fun updateUser(userID : Long, newUser:User): User?

    fun deleteUser(userID:Long): Boolean

    fun getUserByFUID(firebaseUID:String): List<User>
}