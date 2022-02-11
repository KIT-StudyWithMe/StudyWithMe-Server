package ovh.studywithme.server.dao

import ovh.studywithme.server.model.StudyGroup
import ovh.studywithme.server.model.SessionFrequency
import ovh.studywithme.server.model.SessionMode


/**
 * A data access object that contains relevant data about a study-group.
 *
 * @property groupID The study-group's unique identifier.
 * @property name The group's name.
 * @property description The group's description.
 * @property lectureID The lecture's unique identifier the study-group was created for.
 * @property sessionFrequency The projected frequency in which the group will hold study-sessions.
 * @property sessionType The projected mode the study-group wants to meet up in.
 * @property lectureChapter The approximate lecture-chapter the session might address.
 * @property exercise The exercise sheet the session might address.
 * @constructor Create a new StudyGroup
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
     * Convert a StudyGroupDAO to a StudyGroup
     *
     * @param hidden A boolean which is true if the group must not show up in search results in the application.
     * @return corresponding StudyGroup
     */
    fun toStudyGroup(hidden: Boolean): StudyGroup {
        return StudyGroup(this.groupID, this.name, this.description, this.lectureID, this.sessionFrequency, this.sessionType, this.lectureChapter, this.exercise, hidden)
    }
}