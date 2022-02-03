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

    override fun updateUser(userID : Long, newUser : User) : User? {
        var oldUser : User? = userRepository.findById(userID).unwrap()
        if (oldUser == null) return null
        oldUser = newUser
        userRepository.save(oldUser)
        return newUser
        //return userRepository.findById(userID).map { existingUser ->
        //    val updatedUser: User = existingUser
        //            .copy(userName = newUser.userName, contact = newUser.contact)

        //    ResponseEntity.ok().body(userRepository.save(updatedUser))
        //}.orElse(ResponseEntity.notFound().build())
    }

    override fun deleteUser(userID:Long): Boolean {
        val userToDelete : User? = userRepository.findById(userID).unwrap()
        if (userToDelete == null) {
            return false
        } else {
            userRepository.delete(userToDelete)
            return true
        }
        //return userRepository.findById(postId).map { post  ->
        //    userRepository.delete(post)
        //    ResponseEntity<Void>(HttpStatus.OK)
        //}.orElse(ResponseEntity.notFound().build())
    }

    override fun getUserByFUID(firebaseUID:String): List<User> {
        return userRepository.findByfirebaseUID(firebaseUID)
    }

    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}