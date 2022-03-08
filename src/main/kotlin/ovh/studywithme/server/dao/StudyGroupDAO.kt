package ovh.studywithme.server.dao

import ovh.studywithme.server.model.StudyGroup
import ovh.studywithme.server.model.SessionFrequency
import ovh.studywithme.server.model.SessionMode
import ovh.studywithme.server.model.GroupID
import ovh.studywithme.server.model.GroupName
import ovh.studywithme.server.model.GroupDescription
import ovh.studywithme.server.model.LectureID
import ovh.studywithme.server.model.LectureChapter
import ovh.studywithme.server.model.Exercise
import ovh.studywithme.server.repository.GroupRepository


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
    val groupID: GroupID,
    val name: GroupName,
    val description: GroupDescription,
    val lectureID: LectureID,
    val sessionFrequency: SessionFrequency,
    val sessionType: SessionMode,
    val lectureChapter: LectureChapter,
    val exercise: Exercise,
    val memberCount: MemberCount
    ) {

    constructor(group : StudyGroup, memberCount: Int) : this(group.groupID, group.name, group.description, group.lectureID, group.sessionFrequency, group.sessionType, group.lectureChapter, group.exercise, memberCount)

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

/**
 * Member Count
 *
 * @property hidden
 * @constructor Create empty Hidden
 */
@JvmInline
value class MemberCount(private val memberCount: Int)