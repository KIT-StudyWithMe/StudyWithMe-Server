package ovh.studywithme.server.dao

import ovh.studywithme.server.model.StudyGroup
import ovh.studywithme.server.model.SessionFrequency
import ovh.studywithme.server.model.SessionMode


data class StudyGroupDAO(
    val groupID: Long,
    val name: String,
    val description: String,
    val lectureID: Long,
    val sessionFrequency: SessionFrequency,
    val sessionType: SessionMode,
    val lectureChapter: Int,
    val exercise: Int
    ) {
    constructor(group : StudyGroup) : this(group.groupID, group.name, group.description, group.lectureID, group.sessionFrequency, group.sessionType, group.lectureChapter, group.exercise)

    fun toStudyGroup(hidden: Boolean): StudyGroup {
        return StudyGroup(this.groupID, this.name, this.description, this.lectureID, this.sessionFrequency, this.sessionType, this.lectureChapter, this.exercise, hidden)
    }
}