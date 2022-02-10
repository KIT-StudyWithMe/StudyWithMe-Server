package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * Study group member
 *
 * @property studyGroupMemberID
 * @property groupID
 * @property userID
 * @property isAdmin
 * @property isMember
 * @constructor Create empty Study group member
 */
@Entity
data class StudyGroupMember (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val studyGroupMemberID: Long,

    val groupID: Long,

    val userID: Long,

    var isAdmin: Boolean,

    var isMember: Boolean
)