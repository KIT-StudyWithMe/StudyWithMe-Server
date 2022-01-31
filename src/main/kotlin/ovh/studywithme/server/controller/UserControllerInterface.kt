package ovh.studywithme.server.controller

import ovh.studywithme.server.model.User

interface UserControllerInterface {
    fun getAllUsers():List<User>

    fun getUserById(userId:Long):User?

    fun createUser(user:User): User

    fun updateUser(user:User): User?

    fun deleteUser(userID:Long): Boolean
}