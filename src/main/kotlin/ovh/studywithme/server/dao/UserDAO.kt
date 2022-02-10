package ovh.studywithme.server.dao

import ovh.studywithme.server.model.User

/**
 * A data access object that contains restricted information about a user of the application.
 *
 * @property userID The user's unique identifier, which is auto-generated.
 * @property name The name the user chose.
 * @constructor Create empty User d a o
 */
data class UserDAO(
    val userID: Long, 
    val name: String
    ) {
    constructor(user : User) : this(user.userID, user.name)
}