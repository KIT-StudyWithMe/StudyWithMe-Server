package ovh.studywithme.server.dao

import ovh.studywithme.server.model.User

data class StudyGroupMemberDAO(
    val userID: Long,
    val groupID: Long,
    val name: String,
    val isAdmin: Boolean
    ) {
    constructor(user : User, groupID: Long, isAdmin : Boolean) : this(user.userID, groupID, user.name, isAdmin)
}