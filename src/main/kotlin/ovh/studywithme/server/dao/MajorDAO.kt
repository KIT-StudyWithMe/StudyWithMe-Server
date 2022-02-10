package ovh.studywithme.server.dao

import ovh.studywithme.server.model.Major

/**
 * Major d a o
 *
 * @property majorID
 * @property name
 * @constructor Create empty Major d a o
 */
data class MajorDAO(
    val majorID: Long,
    val name: String
    ) {
    constructor(major : Major) : this(major.majorID, major.name)
}