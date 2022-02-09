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

    val lectureID: Int,

    //@get: NotBlank todo, working?
    val sessionFrequency: SessionFrequency,

    //@get: NotBlank todo
    val sessionType: SessionMode,

    val lectureChapter: Int,

    val exercise: Int,

    val hidden: Boolean
)