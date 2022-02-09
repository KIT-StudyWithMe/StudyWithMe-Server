package ovh.studywithme.server.dao

import ovh.studywithme.server.model.Institution

data class InstitutionDAO(
    val institutionID: Long,
    val name: String
    ) {
    constructor(institution : Institution) : this(institution.institutionID, institution.name)
}