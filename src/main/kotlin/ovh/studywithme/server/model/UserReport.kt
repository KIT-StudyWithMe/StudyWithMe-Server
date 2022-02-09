package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class UserReport (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val UserReportID: Long,

    val reporterID: Long = 0,

    val userID: Long = 0,

    //@get: NotBlank //todo? working?
    val userField: UserField
)