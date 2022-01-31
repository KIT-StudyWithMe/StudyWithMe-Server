package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class Lecture (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val lectureID: Int,

    @get: NotBlank
    val name: String,

    @get: NotBlank
    val majorID: Int
)