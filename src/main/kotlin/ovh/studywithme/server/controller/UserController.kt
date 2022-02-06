package ovh.studywithme.server.controller

import ovh.studywithme.server.model.User
import ovh.studywithme.server.repository.UserRepository
import java.util.*
import org.springframework.stereotype.Service

@Service
public class UserController(private val userRepository: UserRepository) : UserControllerInterface {

    override fun getAllUsers():List<User> {
         return userRepository.findAll()
    }

    override fun getUserByID(userID:Long):User? {
        return userRepository.findById(userID).unwrap()
    }

    override fun createUser(user:User): User {
        userRepository.save(user)
        return user
    }

    override fun updateUser(newUser : User) : User? {
        var oldUser : User? = userRepository.findById(newUser.userID).unwrap()
        if (oldUser == null) return null
        oldUser = newUser
        userRepository.save(oldUser)
        return newUser
    }

    override fun deleteUser(userID:Long): Boolean {
        val userToDelete : User? = userRepository.findById(userID).unwrap()
        if (userToDelete == null) {
            return false
        } else {
            userRepository.delete(userToDelete)
            return true
        }
    }

    override fun getUserByFUID(firebaseUID:String): List<User> {
        return userRepository.findByfirebaseUID(firebaseUID)
    }

    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}