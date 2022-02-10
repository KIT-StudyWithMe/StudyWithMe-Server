package ovh.studywithme.server.dao

import ovh.studywithme.server.model.Lecture

/**
 * A data access object that contains relevant data about a lecture held at an institution.
 * Could also be about a course or a seminar, generally whatever a student regards creating a study-group for helpful.
 *
 * @property lectureID The lecture's unique identifier.
 * @property name The lecture's name.
 * @property majorID The student's majorID who created a study-group for this lecture.
 * @constructor Create a new LectureDAO.
 */
data class LectureDAO(
    val lectureID: Long,
    val name: String,
    val majorID: Long
    ) {
    constructor(lecture : Lecture) : this(lecture.lectureID, lecture.name, lecture.majorID)
}