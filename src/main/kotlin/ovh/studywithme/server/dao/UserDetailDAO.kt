package ovh.studywithme.server.dao

import ovh.studywithme.server.model.User

data class UserDetailDAO(
    val userID: Long, 
    val name: String, 
    val institutionID: Long, 
    val majorID: Long, 
    val contact: String, 
    val isModerator: Boolean
    ) {
    constructor(user : User) : this(user.userID, user.name, user.institutionID, user.majorID, user.contact, user.isModerator)
}