package ovh.studywithme.server.view

import ovh.studywithme.server.model.User
import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.controller.UserController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ovh.studywithme.server.model.StudyGroupMember
import ovh.studywithme.server.model.UserField
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserView(private val userController: UserController) {

    @GetMapping("/{id}")
    fun getUserDAO(@PathVariable(value = "id") userID: Long): ResponseEntity<UserDAO> {
        val userDAO : UserDAO? = userController.getUserLightByID(userID)
        if (userDAO != null) return ResponseEntity.ok(userDAO)
        return ResponseEntity.notFound().build()
    }

    @GetMapping("/{id}/detail")
    fun getUserDetailDAO(@PathVariable(value = "id") userID: Long): ResponseEntity<User> {
        val user : User? = userController.getUserByID(userID)
        if (user != null) return ResponseEntity.ok(user)
        return ResponseEntity.notFound().build()
    }

    @GetMapping("")
    fun getAllUsers(@RequestParam("state") state: String?, @RequestParam("FUID") fuid: String?): ResponseEntity<List<User>> {
        if (state.equals("blocked")) {
            val blockedUsers = userController.getBlockedUsers()
            return ResponseEntity.ok(blockedUsers)
        }
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

    @GetMapping("/{id}/groups")
    fun getUsersGroups(@PathVariable(value = "id") userID: Long): ResponseEntity<List<StudyGroupMember>?> {
        val usersGroups = userController.getUsersGroups(userID)
        if (usersGroups == null) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(usersGroups)
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

    @PutMapping("/{uid}/report/{rid}")
    fun reportGroupField(@PathVariable(value = "uid") userID: Long, @PathVariable(value = "rid") reporterID: Long,
                         @Valid @RequestBody field: UserField): ResponseEntity<Void> {
        if (userController.reportUserField(userID, reporterID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    @PutMapping("/{uid}/state/{mid}")
    fun blockUser(@PathVariable(value = "uid") userID: Long, @PathVariable(value = "mid") moderatorID: Long): ResponseEntity<Void> {
        if (userController.blockUser(userID, moderatorID)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

}