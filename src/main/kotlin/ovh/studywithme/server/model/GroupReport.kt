package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class GroupReport (
    @Id
    val userID: Long,

    @Id
    val groupID: Long,

    @Id
    val groupField: GroupField
)