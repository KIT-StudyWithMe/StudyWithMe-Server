package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import java.sql.Date

@Entity
data class Session (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sessionID: Long,

    val groupID: Long,

    @get: NotBlank
    val place: String,

    //@get: NotBlank //todo?
    val startTime: Date,

    val duration: Int
)