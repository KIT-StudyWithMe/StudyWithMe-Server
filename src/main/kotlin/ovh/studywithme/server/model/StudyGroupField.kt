package ovh.studywithme.server.model

/**
 * All fields of a study-group that can contain freetext.
 */
enum class StudyGroupField {
    /**
     * The field "name" in which the group's chosen name can be written.
     */
    NAME,

    /**
     * The field containing the lecture's name the study-group wants to meet for.
     */
    LECTURE,

    /**
     * A description telling a bit about the group, for example who they are, which semester etc.
     */
    DESCRIPTION
}