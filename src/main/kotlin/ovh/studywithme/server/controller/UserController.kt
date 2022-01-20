package ovh.studywithme.server.controller

import ovh.studywithme.server.model.User
import ovh.studywithme.server.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class UserController(private val userRepository: UserRepository) {

    @GetMapping("/users")
    fun getAllUsers(): List<User> =
        userRepository.findAll()


    @PostMapping("/users")
    fun createNewUser(@Valid @RequestBody user: User): User =
        userRepository.save(user)


    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable(value = "id") postId: Long): ResponseEntity<User> {
        return userRepository.findById(postId).map { post ->
            ResponseEntity.ok(post)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/users/{id}")
    fun updateUserById(@PathVariable(value = "id") postId: Long,
                          @Valid @RequestBody newUser: User): ResponseEntity<User> {

        return userRepository.findById(postId).map { existingUser ->
            val updatedUser: User = existingUser
                    .copy(userName = newUser.userName, contact = newUser.contact)

            ResponseEntity.ok().body(userRepository.save(updatedUser))
        }.orElse(ResponseEntity.notFound().build())

    }

    @DeleteMapping("/users/{id}")
    fun deleteUserById(@PathVariable(value = "id") postId: Long): ResponseEntity<Void> {

        return userRepository.findById(postId).map { post  ->
            userRepository.delete(post)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())

    }
}