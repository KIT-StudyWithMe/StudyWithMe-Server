package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * Contains all relevant data about an institution, which is usually a university or a university of applied sciences.
 *
 * @property institutionID The institution's unique identifier, which is auto-generated.
 * @property name The institution's name.
 * @constructor Create a new Institution.
 */
@Entity
data class Institution (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val institutionID: InstitutionID,

    @get: NotBlank
    val name: InstitutionName
)

/**
 * Institution i d
 *
 * @property institutionID
 * @constructor Create empty Lecture i d
 */
@JvmInline
value class InstitutionID(private val institutionID: Long)

/**
 * Institution name
 *
 * @property institutionName
 * @constructor Create empty Lecture i d
 */
@JvmInline
value class InstitutionName(private val institutionName: String)