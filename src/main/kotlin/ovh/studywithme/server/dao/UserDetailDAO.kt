package ovh.studywithme.server.dao

import ovh.studywithme.server.model.User

/**
 * A data access object that contains detailed information about a user of the application.
 *
 * @property userID The user's unique identifier, which is auto-generated.
 * @property name The name the user chose.
 * @property institutionID The unique identifier of the institution the user is enrolled at.
 * @property institutionName The name of the institution the user is enrolled at.
 * @property majorID The unique identifier of the course of studies the user is enrolled in.
 * @property majorName The name of the course of studies the user is enrolled in.
 * @property contact The user's contact information that he wants other users to use to contact him.
 * @property firebaseUID The user's firebase userID token.
 * @property isModerator A boolean that is true if the user has moderator-rights in the application and false otherwise.
 * @constructor Create a new UserDetailDAO
 */
data class UserDetailDAO(
    val userID: Long, 
    val name: String,
    val institutionID: Long,
    val institutionName: String,
    val majorID: Long, 
    val majorName: String,
    val contact: String, 
    val firebaseUID: String,
    val isModerator: Boolean
    ) {
    constructor(user : User, institutionName: String, majorName: String) : this(user.userID, user.name, user.institutionID, institutionName, user.majorID,majorName, user.contact, user.firebaseUID, user.isModerator)

    /**
     * To user
     *
     * @return
     */
    fun toUser(): User {
        return User(this.userID, this.name, this.institutionID, this.majorID, this.contact, this.firebaseUID)
    }
}