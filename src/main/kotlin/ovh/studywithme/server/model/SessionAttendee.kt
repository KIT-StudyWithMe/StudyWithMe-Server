package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.validation.constraints.NotBlank

@Entity
data class SessionAttendee (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val SessionAttendeeID: Long,

    val sessionID: Long = 0,

    val userID: Long = 0,

    @get: NotBlank
    val participates: Boolean
)