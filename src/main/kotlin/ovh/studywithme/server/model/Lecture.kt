package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * Contains all relevant data about a lecture held at an institution. Could also be about a course or a seminar,
 * generally whatever a student regards creating a study-group for helpful.
 *
 * @property lectureID The lecture's unique identifier, which is auto-generated.
 * @property name The lecture's name.
 * @property majorID The student's majorID who created a study-group for this lecture. Used to generate optimized
 * search results for students with the same majorID.
 * @constructor Create a new Lecture.
 */
@Entity
data class Lecture (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val lectureID: Long,

    @get: NotBlank
    val name: String,

    val majorID: Long
)