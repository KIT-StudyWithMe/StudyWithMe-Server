package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * Contains all relevant data about a user of the application.
 *
 * @property userID The user's unique identifier, which is auto-generated.
 * @property name The name the user chose.
 * @property institutionID The unique identifier of the institution the user is enrolled at.
 * @property majorID The unique identifier of the course of studies the user is enrolled in.
 * @property contact The user's contact information that he wants other users to use to contact him.
 * @property firebaseUID The user's firebase userID token.
 * @property isBlocked A boolean that is true if the user is blocked in the application by the moderation and false otherwise.
 * @property isModerator A boolean that is true if the user has moderator-rights in the application and false otherwise.
 * @constructor Create a new User.
 */
@Entity
data class User (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userID: Long,

    @get: NotBlank
    val name: String,

    val institutionID: Long,

    val majorID: Long,

    @get: NotBlank
    val contact: String,

    @get: NotBlank
    val firebaseUID: String,

    var isBlocked: Boolean = false,

    var isModerator: Boolean = false
)