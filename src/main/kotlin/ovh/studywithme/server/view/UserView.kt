package ovh.studywithme.server.view

import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.dao.UserDetailDAO
import ovh.studywithme.server.dao.StudyGroupDAO
import ovh.studywithme.server.controller.UserController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ovh.studywithme.server.model.StudyGroupMember
import ovh.studywithme.server.model.UserField
import javax.validation.Valid

/**
 * The user view is exposed to the client. It is the required way for the client to communicate with the server.
 * All rest-endpoints are defined here and only data access objects are expected and returned.
 * Spring auto-creates a thread for every request and calls the corresponding method.
 *
 * This class bundles all functionality related to users.
 * For data exchange between server and client data access objects must be used.
 *
 * @property userController The server's internal user management logic that the view uses to process the client's requests.
 * @constructor Create a user view, all variables are instanced by Spring's autowire.
 */
@RestController
@RequestMapping("/users")
class UserView(private val userController: UserController) {

    /**
     * Gets a user as data access object from the server, which only contains restricted information about the user,
     * which is for public use in the application.
     * The user is identified by its unique id that the client sends with the request.
     * If the user was found, it will be returned together with http status "200: OK".
     * If it was not found, http status "404: NOT FOUND" will be returned.
     *
     * @param userID The user's unique identifier.
     * @return A data access object containing the user's restricted information.
     */
    @GetMapping("/{id}")
    fun getUserDAO(@PathVariable(value = "id") userID: Long): ResponseEntity<UserDAO> {
        val userDAO : UserDAO? = userController.getUserByID(userID)
        if (userDAO != null) return ResponseEntity.ok(userDAO)
        return ResponseEntity.notFound().build()
    }

    /**
     * Gets a user as data access object from the server, which contains the users complete information.
     * This information set should not be used in public in the application.
     * The user is identified by its unique id that the client sends with the request.
     * If the user was found, it will be returned together with http status "200: OK".
     * If it was not found, http status "404: NOT FOUND" will be returned.
     *
     * @param userID The user's unique identifier.
     * @return A data access object containing the user's complete information.
     */
    @GetMapping("/{id}/detail")
    fun getUserDetailDAO(@PathVariable(value = "id") userID: Long): ResponseEntity<UserDetailDAO> {
        val user : UserDetailDAO? = userController.getUserDetailByID(userID)
        if (user != null) return ResponseEntity.ok(user)
        return ResponseEntity.notFound().build()
    }

    /**
     * Used to receive a list of users from the server. Via parameters adjustments on which users to receive can be made.
     *
     * @param state Set to "blocked" to receive a list of all users that have been blocked by a moderator in the application.
     * @param fuid If the value is not null, the user with the given firebase user id will be returned.
     * @return A list which contains the requested users.
     */
    @GetMapping("")
    fun getAllUsers(@RequestParam("state") state: String?, @RequestParam("FUID") fuid: String?): ResponseEntity<List<UserDAO>> {
        if (state.equals("blocked")) {
            val blockedUsers = userController.getBlockedUsers()
            return ResponseEntity.ok(blockedUsers)
        }
        if (fuid == null) {
            //TODO this cannot exist in final Application
            val users : List<UserDAO> = userController.getAllUsers()
                return ResponseEntity.ok(users)
        }
        else {
            val users : List<UserDAO> =  userController.getUserByFUID(fuid)
            if (!users.isEmpty())
                return ResponseEntity.ok(users)
            else
                return ResponseEntity.notFound().build()
        }
    }

    /**
     * To display a list of the groups a user joined in the application, this function is used.
     * If the user has not joined any groups yet, an empty list will be returned.
     * If the user was not found, null is returned.
     *
     * @param userID The user's unique identifier.
     * @return A list containing all the groups the user is member in.
     */
    @GetMapping("/{id}/groups")
    fun getUsersGroups(@PathVariable(value = "id") userID: Long): ResponseEntity<List<StudyGroupDAO>?> {
        val usersGroups = userController.getUsersGroups(userID)
        if (usersGroups == null) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(usersGroups)
    }

    /**
     * Create new user
     *
     * @param user
     * @return
     */
    @PostMapping("")
    fun createNewUser(@Valid @RequestBody user: UserDetailDAO): UserDetailDAO =
        userController.createUser(user)

    /**
     * Update user by id
     *
     * @param userID
     * @param newUser
     * @return
     */
    @PutMapping("/{id}/detail")
    fun updateUserById(@PathVariable(value = "id") userID: Long, @Valid @RequestBody newUser: UserDetailDAO): ResponseEntity<UserDetailDAO> {
        val user : UserDetailDAO? = userController.updateUser(userID, newUser)
        if (user == null) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }

    /**
     * Delete user by id
     *
     * @param userID
     * @return
     */
    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable(value = "id") userID: Long): ResponseEntity<Void> {
        if(userController.deleteUser(userID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }

    /**
     * Report user field
     *
     * @param userID
     * @param reporterID
     * @param field
     * @return
     */
    @PutMapping("/{uid}/report/{rid}")
    fun reportUserField(@PathVariable(value = "uid") userID: Long, @PathVariable(value = "rid") reporterID: Long,
                         @Valid @RequestBody field: UserField): ResponseEntity<Void> {
        if (userController.reportUserField(userID, reporterID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * Block user
     *
     * @param userID
     * @param moderatorID
     * @return
     */
    @PutMapping("/{uid}/state/{mid}")
    fun blockUser(@PathVariable(value = "uid") userID: Long, @PathVariable(value = "mid") moderatorID: Long): ResponseEntity<Void> {
        if (userController.blockUser(userID, moderatorID)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

}