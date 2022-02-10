package ovh.studywithme.server.dao

import ovh.studywithme.server.model.Institution

/**
 * Institution d a o
 *
 * @property institutionID
 * @property name
 * @constructor Create empty Institution d a o
 */
data class InstitutionDAO(
    val institutionID: Long,
    val name: String
    ) {
    constructor(institution : Institution) : this(institution.institutionID, institution.name)
}