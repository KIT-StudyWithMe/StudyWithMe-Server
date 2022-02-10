package ovh.studywithme.server.dao

import ovh.studywithme.server.model.Lecture

/**
 * Lecture d a o
 *
 * @property lectureID
 * @property name
 * @property majorID
 * @constructor Create empty Lecture d a o
 */
data class LectureDAO(
    val lectureID: Long,
    val name: String,
    val majorID: Long
    ) {
    constructor(lecture : Lecture) : this(lecture.lectureID, lecture.name, lecture.majorID)
}