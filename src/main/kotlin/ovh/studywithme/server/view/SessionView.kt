package ovh.studywithme.server.view

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ovh.studywithme.server.controller.SessionController
import ovh.studywithme.server.dao.SessionAttendeeDAO
import ovh.studywithme.server.model.SessionField
import ovh.studywithme.server.dao.SessionDAO
import ovh.studywithme.server.model.SessionAttendee
import javax.validation.Valid

/**
 * The session view is exposed to the client. It is the required way for the client to communicate with the server.
 * All rest-endpoints are defined here and only data access objects are expected and returned.
 * Spring auto-creates a thread for every request and calls the corresponding method.
 *
 * This class bundles all functionality related to study sessions.
 * For data exchange between server and client data access objects must be used.
 *
 * @property sessionController The server's internal session management logic that the view uses to process the client's requests.
 * @constructor Create a session view, all variables are instanced by Spring's autowire.
 */
@RestController
@RequestMapping("/sessions")
class SessionView(private val sessionController: SessionController) {

    /**
     * Gets a session as data access object from the server.
     * The session is identified by its unique id that the client sends with the request.
     * If the session was found, it will be returned together with http status "200: OK".
     * If it was not found, http status "404: NOT FOUND" will be returned.
     *
     * @param sessionID The requested session's unique identifier.
     * @return  A data access object containing the session's information.
     */
    @GetMapping("/{id}")
    fun getSessionById(@PathVariable(value = "id") sessionID: Long): ResponseEntity<SessionDAO> {
        val session : SessionDAO? = sessionController.getSessionByID(sessionID)
        if (session != null)
            return ResponseEntity.ok(session)
        else
            return ResponseEntity.notFound().build()
    }

    /**
     * Updates an existing study session.
     * The session is identified by its unique id that the client sends with the request.
     * If the session that will be updated was found and updated, it will be returned together with http status "200: OK".
     * If it was not found and therefore could not be updated, http status "404: NOT FOUND" will be returned.
     *
     * @param sessionID The unique identifier for the session that will be updated.
     * @param updatedSession The session with updated content.
     * @return The updated session including the updated content.
     */
    @PutMapping("/{id}")
    fun updateSessionById(@PathVariable(value = "id") sessionID: Long, @Valid @RequestBody updatedSession: SessionDAO): ResponseEntity<SessionDAO> {
        val session : SessionDAO? = sessionController.updateSession(updatedSession)
        if (session != null)
            return ResponseEntity.ok(session)
        else
            return ResponseEntity.notFound().build()
    }

    /**
     * Deletes an existing study session.
     * The session is identified by its unique id that the client sends with the request.
     * If the session was found and deleted, it will be returned together with http status "200: OK".
     * If it was not found and therefore could not be deleted, http status "404: NOT FOUND" will be returned.
     *
     * @param sessionID The unique identifier for the session that will be updated.
     * @return A http status indicating whether the deletion was successful or not.
     */
    @DeleteMapping("/{id}")
    fun deleteSessionById(@PathVariable(value = "id") sessionID: Long): ResponseEntity<Void> {
        if (sessionController.deleteSession(sessionID))
            return ResponseEntity<Void>(HttpStatus.OK)
        else
            return ResponseEntity.notFound().build()
    }

    /**
     * Used when a User decides if he wants to participate in a study session a group he's member in planned
     * and when he wants to change that decision.
     *
     * @param sessionID The unique identifier for the session that will be updated.
     * @param userID The user's unique identifier.
     * @param participates A boolean which is true if the user plans to participate in the session and false if not.
     * @return A http status indicating whether setting the new participation decision was successful or not.
     */
    @PutMapping("/{sid}/participate/{uid}")
    fun setParticipation(@PathVariable(value = "sid") sessionID: Long, @PathVariable(value = "uid") userID: Long,
                         @Valid @RequestBody participates: Boolean): ResponseEntity<Void> {
        if (sessionController.setParticipation(sessionID, userID, participates))
            return ResponseEntity<Void>(HttpStatus.OK)
        else
            return ResponseEntity.notFound().build()
    }

    /**
     * Retrieves a list of all users that confirmed their participation in a study session.
     * The list might be empty.
     * If the session was found, a list of participating users will be returned together with http status "200: OK".
     * If it was not found http status "404: NOT FOUND" will be returned.
     *
     * @param sessionID The session's unique identifier.
     * @return A list of users that will participate in the study session.
     */
    @GetMapping("/{id}/attendee")
    fun getSessionAttendees(@PathVariable(value = "id") sessionID: Long): ResponseEntity<List<SessionAttendeeDAO>> {
        val attendeeList = sessionController.getSessionAttendees(sessionID)
        if (attendeeList != null) {
            return ResponseEntity.ok(sessionController.getSessionAttendees(sessionID))

        }
        return ResponseEntity.notFound().build()
    }

    /**
     * Used when a user wants to report a certain field of a session's details which might contain inappropriate free text.
     * Moderators can review the report later and take appropriate action.
     *
     * @param sessionID The unique identifier for the session that will be updated.
     * @param reporterID The reporting user's unique identifier.
     * @param field The descriptor of the field that's affected.
     * @return A http status indicating whether saving the report was successful or not.
     */
    @PutMapping("/{sid}/report/{uid}")
    fun reportSessionField(@PathVariable(value = "sid") sessionID: Long, @PathVariable(value = "uid") reporterID: Long,
                           @Valid @RequestBody field: SessionField): ResponseEntity<Void> {
        if (sessionController.reportSessionField(sessionID, reporterID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }
}