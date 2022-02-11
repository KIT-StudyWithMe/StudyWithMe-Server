package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * Contains all relevant data about a study-group.
 *
 * @property groupID The study-group's unique identifier, which is auto-generated.
 * @property name The group's name.
 * @property description The group's description.
 * @property lectureID The lecture's unique identifier the study-group was created for.
 * @property sessionFrequency The projected frequency in which the group will hold study-sessions.
 * @property sessionType The projected mode the study-group wants to meet up in.
 * @property lectureChapter The approximate lecture-chapter the session might address.
 * @property exercise The exercise sheet the session might address.
 * @property hidden A boolean which is true if the group shall not be shown in the search results and false otherwise.
 * @constructor Create a new StudyGroup.
 */
@Entity
data class StudyGroup (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val groupID: Long,

    @get: NotBlank
    val name: String,

    @get: NotBlank
    val description: String,

    val lectureID: Long,

    //@get: NotBlank todo, working?
    val sessionFrequency: SessionFrequency,

    //@get: NotBlank todo
    val sessionType: SessionMode,

    val lectureChapter: Int,

    val exercise: Int,

    var hidden: Boolean
)

/**
 * Group i d
 *
 * @property groupID
 * @constructor Create empty Group i d
 */
@JvmInline
value class GroupID(private val groupID: Long)

/**
 * Group name
 *
 * @property name
 * @constructor Create empty Group name
 */
@JvmInline
value class GroupName(private val name: String)

/**
 * Group description
 *
 * @property description
 * @constructor Create empty Group description
 */
@JvmInline
value class GroupDescription(private val description: String)

/**
 * Lecture i d
 *
 * @property lectureID
 * @constructor Create empty Lecture i d
 */
@JvmInline
value class LectureID(private val lectureID: Long)

/**
 * Lecture chapter
 *
 * @property lectureChapter
 * @constructor Create empty Lecture chapter
 */
@JvmInline
value class lectureChapter(private val lectureChapter: Int)

/**
 * Exercise
 *
 * @property exercise
 * @constructor Create empty Exercise
 */
@JvmInline
value class exercise(private val exercise: Int)

/**
 * Hidden
 *
 * @property hidden
 * @constructor Create empty Hidden
 */
@JvmInline
value class hidden(private val hidden: Boolean)
