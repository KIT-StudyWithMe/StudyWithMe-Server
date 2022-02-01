package ovh.studywithme.server.view

import ovh.studywithme.server.model.User
import ovh.studywithme.server.controller.UserController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserView(private val userController: UserController) {

    @GetMapping("")
    fun getAllUsers(): List<User> =  userController.getAllUsers()

    @GetMapping("")
    fun getAllUsers(@RequestParam("FUID") fuid: String): ResponseEntity<User> {
        val user : User? =  userController.getUserByFUID(fuid)
        if (user != null) return ResponseEntity.ok(user)
        return ResponseEntity.notFound().build()
    }


    @PostMapping("")
    fun createNewUser(@Valid @RequestBody user: User): User =
        userController.createUser(user)

    @GetMapping("/{id}")
    fun getUserById(@PathVariable(value = "id") userID: Long): ResponseEntity<User> {
        val user : User? = userController.getUserByID(userID)
        if (user != null) return ResponseEntity.ok(user)
        return ResponseEntity.notFound().build()
    }

    @PutMapping("/{id}")
    fun updateUserById(@PathVariable(value = "id") userID: Long,
                          @Valid @RequestBody newUser: User): ResponseEntity<User> {
        val user : User? = userController.updateUser(newUser)
        if (user == null) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable(value = "id") userID: Long): ResponseEntity<Void> {
        if(userController.deleteUser(userID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }
}