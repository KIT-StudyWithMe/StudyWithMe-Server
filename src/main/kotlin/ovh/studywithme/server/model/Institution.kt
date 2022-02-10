package ovh.studywithme.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

/**
 * Contains all relevant data about an institution. This is usually a university or a university of applied sciences.
 *
 * @property institutionID The institution's unique identifier, which is auto-generated.
 * @property name The institution's name.
 * @constructor Create a new Institution.
 */
@Entity
data class Institution (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val institutionID: Long,

    @get: NotBlank
    val name: String
)