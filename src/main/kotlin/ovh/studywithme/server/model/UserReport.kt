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

    //@Id //currently not working
    val userID: Long,

    //@get: NotBlank //todo? working?
    val userField: UserField
)