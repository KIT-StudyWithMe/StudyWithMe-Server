package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * Contains all relevant data about a degree course / course of studies.
 *
 * @property majorID The major's unique identifier, which is auto-generated.
 * @property name The major's name.
 * @constructor Create a new Major.
 */
@Entity
data class Major (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val majorID: MajorID,

    @get: NotBlank
    val name: MajorName
)

/**
 * Major i d
 *
 * @property majorID
 * @constructor Create empty Lecture i d
 */
@JvmInline
value class MajorID(private val majorID: Long)

/**
 * Major name
 *
 * @property majorName
 * @constructor Create empty Lecture i d
 */
@JvmInline
value class MajorName(private val majorName: String)