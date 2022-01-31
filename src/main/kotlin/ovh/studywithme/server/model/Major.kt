package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class Major (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val majorID: Long,

    @get: NotBlank
    val name: String
)