package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * Contains all relevant data about a user's report regarding a session.
 *
 * @property sessionReportID The session-report's unique identifier, which is auto-generated.
 * @property sessionID The study-session's unique identifier.
 * @property reporterID The reporting user's unique identifier.
 * @property sessionField The session's freetext field which was reported.
 * @constructor Create a new SessionReport.
 */
@Entity
data class SessionReport (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sessionReportID: Long,

    val sessionID: Long = 0,

    val reporterID: Long = 0,

    //@get: NotBlank //todo? working?
    val sessionField: SessionField
)