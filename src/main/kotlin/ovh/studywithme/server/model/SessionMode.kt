package ovh.studywithme.server.model

/**
 * The projected mode the study-group wants to meet up in.
 *
 * @constructor Create empty Session mode
 */
enum class SessionMode {
    /**
     * Sessions will be held online.
     */
    ONLINE,

    /**
     * Sessions will be held in presence.
     */
    PRESENCE,

    /**
     * Sessions will switch between online- and presence-mode or use both.
     */
    HYBRID
}