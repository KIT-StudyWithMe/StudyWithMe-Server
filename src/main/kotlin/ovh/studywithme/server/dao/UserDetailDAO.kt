package ovh.studywithme.server.dao

import ovh.studywithme.server.model.User

/**
 * User detail d a o
 *
 * @property userID
 * @property name
 * @property institutionID
 * @property institutionName
 * @property majorID
 * @property majorName
 * @property contact
 * @property isModerator
 * @constructor Create empty User detail d a o
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
    
    fun toUser(): User {
        return User(this.userID, this.name, this.institutionID, this.majorID, this.contact, this.firebaseUID)
    }
}