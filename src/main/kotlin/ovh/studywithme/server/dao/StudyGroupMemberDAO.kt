package ovh.studywithme.server.dao

import ovh.studywithme.server.model.User

/**
 * Study group member d a o
 *
 * @property userID
 * @property groupID
 * @property name
 * @property isAdmin
 * @constructor Create empty Study group member d a o
 */
data class StudyGroupMemberDAO(
    val userID: Long,
    val groupID: Long,
    val name: String,
    val isAdmin: Boolean
    ) {
    constructor(user : User, groupID: Long, isAdmin : Boolean) : this(user.userID, groupID, user.name, isAdmin)
}