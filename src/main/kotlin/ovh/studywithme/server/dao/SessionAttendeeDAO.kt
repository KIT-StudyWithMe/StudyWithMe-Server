package ovh.studywithme.server.dao

import ovh.studywithme.server.model.SessionAttendee

/**
 *
 * A data access object that contains relevant data about a study-session' attendee.
 * An attendee is someone that made a decision regarding his participation in a study-session
 *
 * @property sessionAttendeeID The unique identifier of the session's attendee, which is auto-generated.
 * @property sessionID The session's unique identifier.
 * @property userID The user's unique identifier.
 * @property participates A boolean which is true if the user wants to participate in the session and false otherwise.
 * @constructor Create a new SessionAttendee.
 */
data class SessionAttendeeDAO (
    val sessionAttendeeID: Long,
    val sessionID: Long,
    val userID: Long,
    val participates: Boolean) {

    constructor(sessionAttendee: SessionAttendee) : this(
        sessionAttendee.sessionAttendeeID,
        sessionAttendee.sessionID,
        sessionAttendee.userID,
        sessionAttendee.participates
    )

    /**
     * Convert a SessionAttendeeDAO to a SessionAttendee.
     *
     * @return corresponding SessionAttendee object.
     */
    fun toSessionAttendee(): SessionAttendee {
        return SessionAttendee(this.sessionAttendeeID, this.sessionID, this.userID, this.participates)
    }
}
