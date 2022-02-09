package ovh.studywithme.server.dao

import ovh.studywithme.server.model.Major

data class MajorDAO(
    val majorID: Long,
    val name: String
    ) {
    constructor(major : Major) : this(major.majorID, major.name)
}