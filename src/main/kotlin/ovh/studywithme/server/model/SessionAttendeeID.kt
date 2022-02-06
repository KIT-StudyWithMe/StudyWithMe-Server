package ovh.studywithme.server.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class SessionAttendeeID(
    // Ich hab keine Ahnung was hier als @ notiert werden muss. Wenn ich nix hinschreibe gibts nen Fehler.
    @Column
    val sessionID: Long = 0,
    @Column
    val userID: Long = 0
) : Serializable