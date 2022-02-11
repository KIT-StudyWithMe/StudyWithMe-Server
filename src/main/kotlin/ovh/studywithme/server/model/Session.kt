package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import java.sql.Date

/**
 * Contains all relevant data about a study-group's study-session.
 *
 * @property sessionID The session's unique identifier, which is auto-generated.
 * @property groupID The unique identifier of the group holding the session.
 * @property place The location where the session will take place.
 * @property startTime The exact time when the session will start.
 * @property duration The session's estimated duration.
 * @constructor Create a new Session.
 */
@Entity
data class Session (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sessionID: Long,

    val groupID: Long,

    @get: NotBlank
    val place: String,

    val startTime: Long,

    val duration: Int
)