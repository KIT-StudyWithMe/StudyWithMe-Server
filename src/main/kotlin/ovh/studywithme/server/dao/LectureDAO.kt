package ovh.studywithme.server.dao

import ovh.studywithme.server.model.Lecture

data class LectureDAO(
    val lectureID: Long,
    val name: String,
    val majorID: Long
    ) {
    constructor(lecture : Lecture) : this(lecture.lectureID, lecture.name, lecture.majorID)
}