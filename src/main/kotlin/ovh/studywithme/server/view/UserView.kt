package ovh.studywithme.server.view

import ovh.studywithme.server.model.User
import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.controller.UserController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserView(private val userController: UserController) {

    @GetMapping("/{id}")
    fun getUserLightById(@PathVariable(value = "id") userID: Long): ResponseEntity<UserDAO> {
        val userLight : UserDAO? = userController.getUserLightByID(userID)
        if (userLight != null) return ResponseEntity.ok(userLight)
        return ResponseEntity.notFound().build()
    }

    @GetMapping("/{id}/detail")
    fun getUserById(@PathVariable(value = "id") userID: Long): ResponseEntity<User> {
        val user : User? = userController.getUserByID(userID)
        if (user != null) return ResponseEntity.ok(user)
        return ResponseEntity.notFound().build()
    }

    @GetMapping("")
    fun getAllUsers(@RequestParam("FUID") fuid: String?): ResponseEntity<List<User>> {
        if (fuid == null) {
            //TODO this cannot exist in final Application
            val users : List<User> = userController.getAllUsers()
                return ResponseEntity.ok(users)
        }
        else {
            val users : List<User> =  userController.getUserByFUID(fuid)
            if (!users.isEmpty())
                return ResponseEntity.ok(users)
            else
                return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("")
    fun createNewUser(@Valid @RequestBody user: User): User =
        userController.createUser(user)

    @PutMapping("/{id}/detail")
    fun updateUserById(@PathVariable(value = "id") userID: Long, @Valid @RequestBody newUser: User): ResponseEntity<User> {
        val user : User? = userController.updateUser(userID, newUser)
        if (user == null) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable(value = "id") userID: Long): ResponseEntity<Void> {
        if(userController.deleteUser(userID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }
}