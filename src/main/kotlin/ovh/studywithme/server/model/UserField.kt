package ovh.studywithme.server.model

/**
 * All fields of a user that can contain freetext.
 */
enum class UserField {
    /**
     * The field containing the user's chosen name.
     */
    NAME,

    /**
     * The field containing the institution's name the user is enrolled at.
     */
    INSTITUTION,

    /**
     * The field containing the name of course of studies the user is enrolled in.
     */
    MAJOR,

    /**
     * The user's contact information that he wants other users to use to contact him.
     */
    CONTACT
}