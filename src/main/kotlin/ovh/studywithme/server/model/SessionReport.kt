package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * Session report
 *
 * @property sessionReportID
 * @property sessionID
 * @property reporterID
 * @property sessionField
 * @constructor Create empty Session report
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