package ovh.studywithme.server.dao

import ovh.studywithme.server.model.User
import ovh.studywithme.server.model.StudyGroupMember

/**
 * A data access object that contains relevant data about a study-group's member for the client.
 *
 * @property userID The user's unique identifier.
 * @property groupID The unique identifier of the according group the user is a member in.
 * @property name The group member's name.
 * @property isAdmin A boolean which is true if the user is not only a member but also an administrator of the according group.
 * @constructor Create a new StudyGroupMemberDAO.
 */
data class StudyGroupMemberDAO(
    val userID: Long,
    val groupID: Long,
    val name: String,
    val isAdmin: Boolean
    ) {
    constructor(user : User, groupID: Long, isAdmin : Boolean) : this(user.userID, groupID, user.name, isAdmin)
    constructor(member : StudyGroupMember, username: String) : this(member.userID, member.groupID, username, member.isAdmin)
}