package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class UserReport (
    @Id
    val reporterID: Long,

    @Id
    val userID: Long,

    @get: NotBlank
    val userField: UserField
)