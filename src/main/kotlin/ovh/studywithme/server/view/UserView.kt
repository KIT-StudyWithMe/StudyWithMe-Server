package ovh.studywithme.server.view

import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.dao.UserDetailDAO
import ovh.studywithme.server.dao.StudyGroupDAO
import ovh.studywithme.server.controller.UserController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
     * This method is executed when GET /users/{id} is called.
     *
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
     * This method is executed when GET /users/{id}/detail is called.
     *
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
     * This method is executed when GET /users is called.
     *
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
     * This method is executed when GET /users/{id}/groups is called.
     *
     * To display a list of the groups a user joined in the application, this function is used.
     * If the user has not joined any groups yet, an empty list will be returned.
     * If the user was not found, null is returned together with http status "404: NOT FOUND".
     * If the user was found, http status "200: OK" will be returned in addition to the list.
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
     * This method is executed when POST /users is called.
     *
     * Creates a new user. Used when a new user completed the application's registration.
     * Use 0 as userID for new users here.
     * The id he got from the server will be returned with the return object.
     *
     * @param user User information with which the new user should be created.
     * @return The user that was created on the server, including his correct userID.
     */
    @PostMapping("")
    fun createNewUser(@Valid @RequestBody user: UserDetailDAO): ResponseEntity<UserDetailDAO> =
        ResponseEntity.ok(userController.createUser(user))

    /**
     * This method is executed when PUT /users/{id}/detail is called.
     *
     * Update user with the Information provided.
     * Used when a user updates his profile information in the application.
     * If there is no User with the userID then the http status "404: NOT FOUND" is returned.
     * If the operation was successful then http status "200: OK" is returned.
     *
     * @param userID The unique identifier of the user that should be updated.
     * @param newUser The updated user information.
     * @return The updated user together with a http status.
     */
    @PutMapping("/{id}/detail")
    fun updateUserById(@PathVariable(value = "id") userID: Long, @Valid @RequestBody newUser: UserDetailDAO): ResponseEntity<UserDetailDAO> {
        val user : UserDetailDAO? = userController.updateUser(userID, newUser)
        if (user == null) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }

    /**
     * This method is executed when DELETE /users/{id} is called.
     *
     * Deletes a user from the server. Used when a user wants to delete his account.
     * A http status as return value indicates if the operation was successful.
     *
     * @param userID The user's unique identifier.
     * @return http status "200: OK" if the user was successfully deleted and http status "404: NOT FOUND" otherwise.
     */
    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable(value = "id") userID: Long): ResponseEntity<Void> {
        if(userController.deleteUser(userID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when PUT /users/{id}/report/{id} is called.
     *
     * As a user's details contain freetext, the freetext fields might contain inappropriate text.
     * Therefore, all users are able to report such inappropriate text to the moderation of the application.
     * This method is used to report a text field of another user.
     *
     * @param userID The reported user's unique identifier.
     * @param reporterID The reporting user's unique identifier.
     * @param field The descriptor of the field in a user's details that is being reported.
     * @return http status "200: OK" if the report was successfully stored and http status "404: NOT FOUND" otherwise.
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
     * This method is executed when PUT /users/{id}/state/{id} is called.
     *
     * If a user keeps putting inappropriate texts in his profile page or is a continues negative impact on the
     * experience for other users, a moderator might want to block that user.
     * This method is used for such a block of a user that is a nuisance to others.
     *
     * @param userID The unique identifier of the user that will be blocked.
     * @param moderatorID The moderator's unique identifier that will block the user.
     * @return http status "200: OK" if the operation was successfully deleted and http status "404: NOT FOUND" otherwise.
     */
    @PutMapping("/{uid}/state/{mid}")
    fun blockUser(@PathVariable(value = "uid") userID: Long, @PathVariable(value = "mid") moderatorID: Long): ResponseEntity<Void> {
        if (userController.blockUser(userID, moderatorID)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

}