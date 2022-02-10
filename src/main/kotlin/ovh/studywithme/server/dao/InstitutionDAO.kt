package ovh.studywithme.server.dao

import ovh.studywithme.server.model.Institution

/**
 * A data access object that contains relevant data about an institution,
 * which is usually a university or a university of applied sciences.
 *
 * @property institutionID The institution's unique identifier.
 * @property name The institution's name.
 * @constructor Create a new InstitutionDAO.
 */
data class InstitutionDAO(
    val institutionID: Long,
    val name: String
    ) {
    constructor(institution : Institution) : this(institution.institutionID, institution.name)

    /**
     * Convert a InstitutionDAO to a Institution
     *
     * @return corresponding Institution
     */
    fun toInstitution(): Institution {
        return Institution(this.institutionID, this.name)
    }
}