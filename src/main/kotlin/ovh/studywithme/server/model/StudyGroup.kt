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

    @get: NotBlank
    val lectureID: Int,

    @get: NotBlank
    val sessionFrequency: SessionFrequency,

    @get: NotBlank
    val sessionType: SessionMode,

    @get: NotBlank
    val lectureChapter: Int,

    @get: NotBlank
    val exercise: Int,

    @get: NotBlank
    val hidden: Boolean
)