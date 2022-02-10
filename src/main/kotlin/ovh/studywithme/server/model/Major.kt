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
 * @property name The Major's name.
 * @constructor Create a new Major.
 */
@Entity
data class Major (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val majorID: Long,

    @get: NotBlank
    val name: String
)