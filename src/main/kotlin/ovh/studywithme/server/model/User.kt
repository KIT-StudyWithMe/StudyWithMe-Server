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
    val userID: UserID,

    val name: UserName,

    val institutionID: InstitutionID,

    val majorID: MajorID,

    val contact: UserContact,

    val firebaseUID: FirebaseUID,

    val isBlocked: IsBlocked,

    var isModerator: IsModerator
)

/**
 * User i d
 *
 * @property userID
 * @constructor Create empty Group i d
 */
@JvmInline
value class UserID(private val userID: Long)

/**
 * User name
 *
 * @property userName
 * @constructor Create empty Group i d
 */
@JvmInline
value class UserName(private val userName: String)

/**
 * UserContact
 *
 * @property userContact
 * @constructor Create empty Group i d
 */
@JvmInline
value class UserContact(private val UserContact: String)

/**
 * FirebaseUID
 *
 * @property firebaseUID
 * @constructor Create empty Group i d
 */
@JvmInline
value class FirebaseUID(private val firebaseUID: String)

/**
 * Is Blocked
 *
 * @property isBlocked
 * @constructor Create empty Group i d
 */
@JvmInline
value class IsBlocked(private val isBlocked: Boolean = false) {
    fun toBoolean():Boolean=this.isBlocked
}

/**
 * IsModerator
 *
 * @property isModerator
 * @constructor Create empty Group i d
 */
@JvmInline
value class IsModerator(private val isModerator: Boolean = false) {
    fun toBoolean():Boolean=this.isModerator
}