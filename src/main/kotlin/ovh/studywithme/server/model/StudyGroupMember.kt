package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class StudyGroupMember (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val StudyGroupMemberID: Long,

    val groupID: Long,

    val userID: Long,

    var isAdmin: Boolean,

    var isMember: Boolean
)