package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class SessionAttendee (
    @Id
    val sessionID: Long,

    //@Id //not working currently
    val userID: Long,

    @get: NotBlank
    val participates: Boolean
)