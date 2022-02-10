package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.dao.UserDetailDAO
import ovh.studywithme.server.dao.StudyGroupDAO
import ovh.studywithme.server.model.UserField

/**
 * User controller interface that contains all methods needed for user-management.
 *
 * @constructor Create empty User controller interface
 */
interface UserControllerInterface {
    /**
     * Get all users. Returns an empty List if there are no Users.
     *
     * @return List of all Users
     */
    fun getAllUsers():List<UserDAO>

    /**
     * Get detailed User information about the user with the specified ID
     * If there is no user with that ID, null is returned
     *
     * @param userID UserID which details should be fetched
     * @return Detailed User Information
     */
    fun getUserDetailByID(userID:Long):UserDetailDAO?

    /**
     * Get basic user information about the user with the specified ID
     * If there is no user with that ID, null is returned
     *
     * @param userID UserID which details should be fetched
     * @return Basic User Information
     */
    fun getUserByID(userID:Long):UserDAO?

    /**
     * Get all Groups the User is in.
     * If the user is not found null is returned.
     * If the user has no Groups an empty List is returned.
     *
     * @param userID UserID of the User which groups should be returned
     * @return List of all groups the user is in
     */
    fun getUsersGroups(userID:Long): List<StudyGroupDAO>?

    /**
     * Create a new User. You have to choose 0 as UserID.
     * The method returns the newly created user with the generated UserID
     *
     * @param userDetailDAO UserInformation with which the User should be created
     * @return The User that was created
     */
    fun createUser(userDetailDAO:UserDetailDAO): UserDetailDAO

    /**
     * Update user with the Information provided.
     * If there is no User with the userID then null is returned.
     * If it was successful the updated User is returned.
     *
     * @param userID The User ID of the user that should be updated
     * @param updatedUser The new User-contents that the user should have
     * @return The updated User with the new contents
     */
    fun updateUser(userID: Long, updatedUser:UserDetailDAO): UserDetailDAO?

    /**
     * Delete user by its ID.
     * If there is no User with that ID then false is returned.
     * If there is a user with that ID then it gets deleted and true is returned.
     *
     * @param userID The userID of the user that should get deleted
     * @return True if the user existed and is now deleted, False if the user didn't exist
     */
    fun deleteUser(userID:Long): Boolean

    /**
     * Searches in the Database for Users with that Firebase User ID.
     * It returns a List of matches. If no match is found an empty List is Returned
     *
     * @param firebaseUID FirebaseUID to search for
     * @return All Users that have exactly this FirebaseUID
     */
    fun getUserByFUID(firebaseUID:String): List<UserDAO>

    /**
     * Reports a UserField. 
     * If the userID, the reporterID and the field is found, then the Field gets reported and true is returned.
     * If one of them is not found false is returned.
     *
     * @param userID UserID of the user with the field that should be reported
     * @param reporterID UserID of the user that creates the report
     * @param field Field that should be reported
     * @return True if the Field was reported, False if the Field was not found
     */
    fun reportUserField(userID:Long, reporterID:Long, field: UserField): Boolean

    /**
     * Mark user as blocked.
     * If the userID and the userID of the Moderator exist then the user gets blocked and true is returned.
     * If either of them is not found false is returned.
     *
     * @param userID UserID of the User which should get blocked
     * @param moderatorID UserID of the User that is a Moderator and requests the blocking
     * @return True if the User is now blocked, False if either of the two users was not found or had wrong permissions
     */
    fun blockUser(userID:Long, moderatorID:Long): Boolean

    /**
     * Returns a list of blocked Users.
     *
     * @return List of all Users that are blocked
     */
    fun getBlockedUsers(): List<UserDAO>
}