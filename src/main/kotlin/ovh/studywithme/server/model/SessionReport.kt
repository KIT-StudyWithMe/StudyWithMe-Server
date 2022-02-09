package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class SessionReport (
    @Id
    val sessionID: Long,

    //@Id //currently not working
    val reporterID: Long,

    //@get: NotBlank //todo? working?
    val sessionField: SessionField
)