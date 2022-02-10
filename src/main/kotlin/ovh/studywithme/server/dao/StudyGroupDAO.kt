package ovh.studywithme.server.dao

import ovh.studywithme.server.model.StudyGroup
import ovh.studywithme.server.model.SessionFrequency
import ovh.studywithme.server.model.SessionMode


/**
 * Study group d a o
 *
 * @property groupID
 * @property name
 * @property description
 * @property lectureID
 * @property sessionFrequency
 * @property sessionType
 * @property lectureChapter
 * @property exercise
 * @constructor Create empty Study group d a o
 */
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

    /**
     * To study group
     *
     * @param hidden
     * @return
     */
    fun toStudyGroup(hidden: Boolean): StudyGroup {
        return StudyGroup(this.groupID, this.name, this.description, this.lectureID, this.sessionFrequency, this.sessionType, this.lectureChapter, this.exercise, hidden)
    }
}