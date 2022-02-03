package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class GroupMember (
    @Id
    val groupID: Long,

    //@Id //currently not working
    val userID: Long,

    @get: NotBlank
    val isAdmin: Boolean,

    @get: NotBlank
    val isMember: Boolean
)