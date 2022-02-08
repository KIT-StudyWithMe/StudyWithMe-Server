package ovh.studywithme.server.controller

import ovh.studywithme.server.model.User
import ovh.studywithme.server.dao.UserLight
import ovh.studywithme.server.repository.UserRepository
import java.util.Optional
import org.springframework.stereotype.Service

@Service
    class UserController(private val userRepository: UserRepository) : UserControllerInterface {

    override fun getAllUsers():List<User> {
         return userRepository.findAll()
    }

    override fun getUserByID(userID:Long):User? {
        return userRepository.findById(userID).unwrap()
    }

    override fun getUserLightByID(userID:Long):UserLight? {
        val user : User? = userRepository.findById(userID).unwrap()
        if (user != null) {
            return UserLight(user)
        }
        return null
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

    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}