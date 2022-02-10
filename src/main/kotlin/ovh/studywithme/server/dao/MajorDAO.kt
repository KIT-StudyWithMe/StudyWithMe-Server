package ovh.studywithme.server.dao

import ovh.studywithme.server.model.Major

/**
 * A data access object that contains relevant data about a degree course / course of studies.
 *
 * @property majorID The major's unique identifier.
 * @property name The major's name.
 * @constructor Create a new MajorDAO.
 */
data class MajorDAO(
    val majorID: Long,
    val name: String
    ) {
    constructor(major : Major) : this(major.majorID, major.name)
}