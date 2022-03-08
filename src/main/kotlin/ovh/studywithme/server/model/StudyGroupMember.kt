package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * Contains all data about a study-group's member.
 *
 * @property studyGroupMemberID The member's unique identifier, which is auto-generated. It's unique across all users
 * and groups.
 * @property groupID The unique identifier of the according group the user is a member in.
 * @property userID The user's unique identifier.
 * @property isAdmin A boolean which is true if the user is not only a member but also an administrator of the according group.
 * @property isMember A boolean which is true if the user is a member of the group and false if he sent an application only.
 * @constructor Create a new StudyGroupMember.
 */
@Entity
data class StudyGroupMember (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memberID: GroupMemberID,

    val groupID: GroupID,

    val userID: UserID,

    var isAdmin: IsAdmin,

    var isMember: IsMember
)

/**
 * GroupMember i d
 *
 * @property groupMemberID
 * @constructor Create empty Group i d
 */
@JvmInline
value class GroupMemberID(private val groupMemberID: Long)

/**
 * Is admin
 *
 * @property isAdmin
 * @constructor Create empty Group i d
 */
@JvmInline
value class IsAdmin(private val isAdmin: Boolean) {
    fun toBoolean():Boolean=this.isAdmin
}

/**
 * isMember
 *
 * @property isMember
 * @constructor Create empty Group i d
 */
@JvmInline
value class IsMember(private val isMember: Boolean) {
    fun toBoolean():Boolean=this.isMember
}