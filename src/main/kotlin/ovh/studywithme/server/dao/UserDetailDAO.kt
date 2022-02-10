package ovh.studywithme.server.dao

import ovh.studywithme.server.model.User

data class UserDetailDAO(
    val userID: Long, 
    val name: String, 
    val institutionID: Long,
    val institutionName: String,
    val majorID: Long, 
    val majorName: String,
    val contact: String, 
    val isModerator: Boolean
    ) {
    constructor(user : User, institutionName: String, majorName: String) : this(user.userID, user.name, user.institutionID, institutionName, user.majorID,majorName, user.contact, user.isModerator)
}