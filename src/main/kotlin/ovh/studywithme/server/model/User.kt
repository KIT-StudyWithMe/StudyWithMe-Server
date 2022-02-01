package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class User (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userID: Long,

    @get: NotBlank
    val name: String,

    @get: NotBlank
    val collegeID: Int,

    @get: NotBlank
    val majorID: Int,

    @get: NotBlank
    val contact: String,

    @get: NotBlank
    val firebaseUID: String
)