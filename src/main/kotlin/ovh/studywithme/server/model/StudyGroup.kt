package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

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

    val hidden: Boolean
)

@JvmInline
value class GroupID(private val groupID: Long)

@JvmInline
value class GroupName(private val name: String)

@JvmInline
value class GroupDescription(private val description: String)

@JvmInline
value class LectureID(private val lectureID: Long)

@JvmInline
value class lectureChapter(private val lectureChapter: Int)

@JvmInline
value class exercise(private val exercise: Int)

@JvmInline
value class hidden(private val hidden: Boolean)
