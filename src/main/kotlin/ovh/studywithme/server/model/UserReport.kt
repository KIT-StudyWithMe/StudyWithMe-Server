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
 * @constructor Create a new UserReport.
 */
@Entity
data class UserReport (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userReportID: UserReportID,

    val reporterID: UserID,

    val userID: UserID,

    val userField: UserField
)

/**
 * UserReportID
 *
 * @property userReportID
 * @constructor Create empty Group i d
 */
@JvmInline
value class UserReportID(private val userReportID: Long)
