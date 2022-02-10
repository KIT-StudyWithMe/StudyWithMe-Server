package ovh.studywithme.server.controller

import ovh.studywithme.server.model.User
import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.model.StudyGroupMember
import ovh.studywithme.server.model.UserField

/**
 * User controller interface
 *
 * @constructor Create empty User controller interface
 */
interface UserControllerInterface {
    /**
     * Get all users
     *
     * @return
     */
    fun getAllUsers():List<User>

    /**
     * Get user by i d
     *
     * @param userID
     * @return
     */
    fun getUserByID(userID:Long):User?

    /**
     * Get user light by i d
     *
     * @param userID
     * @return
     */
    fun getUserLightByID(userID:Long):UserDAO?

    /**
     * Get users groups
     *
     * @param userID
     * @return
     */
    fun getUsersGroups(userID:Long): List<StudyGroupMember>?

    /**
     * Create user
     *
     * @param user
     * @return
     */
    fun createUser(user:User): User

    /**
     * Update user
     *
     * @param userID
     * @param updatedUser
     * @return
     */
    fun updateUser(userID: Long, updatedUser:User): User?

    /**
     * Delete user
     *
     * @param userID
     * @return
     */
    fun deleteUser(userID:Long): Boolean

    /**
     * Get user by f u i d
     *
     * @param firebaseUID
     * @return
     */
    fun getUserByFUID(firebaseUID:String): List<User>

    /**
     * Report user field
     *
     * @param userID
     * @param reporterID
     * @param field
     * @return
     */
    fun reportUserField(userID:Long, reporterID:Long, field: UserField): Boolean

    /**
     * Block user
     *
     * @param userID
     * @param moderatorID
     * @return
     */
    fun blockUser(userID:Long, moderatorID:Long): Boolean

    /**
     * Get blocked users
     *
     * @return
     */
    fun getBlockedUsers(): List<User>
}