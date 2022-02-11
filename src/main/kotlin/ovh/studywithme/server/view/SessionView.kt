package ovh.studywithme.server.view

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ovh.studywithme.server.controller.SessionController
import ovh.studywithme.server.model.SessionField
import ovh.studywithme.server.dao.SessionDAO
import javax.validation.Valid

/**
 * The session view is exposed to the client. It is the required way for the client to communicate with the server.
 * All rest-endpoints are defined here and only data access objects are expected and returned.
 * This class bundles all functionality related to where and what a user studies.
 * Spring auto-creates a thread for every request and calls the corresponding method.
 *
 * @property sessionController The server's internal session management logic that the view uses to process the client's requests.
 * @constructor Create a session view, all variables are instanced by Spring's autowire.
 */
@RestController
@RequestMapping("/sessions")
class SessionView(private val sessionController: SessionController) {

    /**
     * Get session by id
     *
     * @param sessionID
     * @return
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
     * Update session by id
     *
     * @param sessionID
     * @param updatedSession
     * @return
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
     * Delete session by id
     *
     * @param sessionID
     * @return
     */
    @DeleteMapping("/{id}")
    fun deleteSessionById(@PathVariable(value = "id") sessionID: Long): ResponseEntity<Void> {
        if (sessionController.deleteSession(sessionID))
            return ResponseEntity<Void>(HttpStatus.OK)
        else
            return ResponseEntity.notFound().build()
    }

    /**
     * Set participation
     *
     * @param sessionID
     * @param userID
     * @param participates
     * @return
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
     * Report session field
     *
     * @param sessionID
     * @param reporterID
     * @param field
     * @return
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