package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * Lecture
 *
 * @property lectureID
 * @property name
 * @property majorID
 * @constructor Create empty Lecture
 */
@Entity
data class Lecture (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val lectureID: Long,

    @get: NotBlank
    val name: String,

    val majorID: Long
)