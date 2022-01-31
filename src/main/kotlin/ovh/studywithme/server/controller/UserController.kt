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

    override fun getUserById(userId:Long):User? {
        return userRepository.findById(userId).unwrap()
    }

    override fun createUser(user:User): User {
        return user
    }

    override fun updateUser(user:User): User? {
        return user
        //return userRepository.findById(userID).map { existingUser ->
        //    val updatedUser: User = existingUser
        //            .copy(userName = newUser.userName, contact = newUser.contact)

        //    ResponseEntity.ok().body(userRepository.save(updatedUser))
        //}.orElse(ResponseEntity.notFound().build())
    }

    override fun deleteUser(userID:Long): Boolean {
        return true
        //return userRepository.findById(postId).map { post  ->
        //    userRepository.delete(post)
        //    ResponseEntity<Void>(HttpStatus.OK)
        //}.orElse(ResponseEntity.notFound().build())
    }

    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}