package ovh.studywithme.server.dao

import ovh.studywithme.server.model.User

/**
 * User d a o
 *
 * @property userID
 * @property name
 * @constructor Create empty User d a o
 */
data class UserDAO(
    val userID: Long, 
    val name: String
    ) {
    constructor(user : User) : this(user.userID, user.name)
}