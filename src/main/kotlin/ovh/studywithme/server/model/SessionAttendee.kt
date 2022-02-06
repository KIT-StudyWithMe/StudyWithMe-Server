package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.validation.constraints.NotBlank

@Entity
@IdClass(SessionAttendeeID::class)
data class SessionAttendee (
    @Id
    val sessionID: Long = 0,

    @Id
    val userID: Long = 0,

    @get: NotBlank
    val participates: Boolean
)