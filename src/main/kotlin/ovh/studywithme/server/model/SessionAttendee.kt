package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.validation.constraints.NotBlank

/**
 * Contains all relevant data about a study session's attendee.
 * Basically, stores a user's decision about participating in said group's study-session.
 *
 * @property sessionAttendeeID The unique identifier of the session's attendee, which is auto-generated.
 * @property sessionID The session's unique identifier.
 * @property userID The user's unique identifier.
 * @property participates A boolean which is true if the user wants to participate in the session and false otherwise.
 * @constructor Create a new SessionAttendee.
 */
@Entity
data class SessionAttendee (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sessionAttendeeID: Long,

    val sessionID: Long = 0,

    val userID: Long = 0,

    var participates: Boolean
)