package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * Contains all relevant data about a user's report regarding another user.
 *
 * @property userReportID The user-report's unique identifier, which is auto-generated.
 * @property reporterID The reporting user's unique identifier.
 * @property userID The reported user's unique identifier.
 * @property userField The reported user's freetext field which was reported.
 * @constructor Create a new UserReport
 */
@Entity
data class UserReport (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userReportID: Long,

    val reporterID: Long = 0,

    val userID: Long = 0,

    //@get: NotBlank //todo? working?
    val userField: UserField
)