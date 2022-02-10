import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ovh.studywithme.server.controller.SessionController
import ovh.studywithme.server.model.Session
import ovh.studywithme.server.model.SessionField
import javax.validation.Valid

@RestController
@RequestMapping("/sessions")
class SessionView(private val sessionController: SessionController) {

    @GetMapping("/{id}")
    fun getSessionById(@PathVariable(value = "id") sessionID: Long): ResponseEntity<Session> {
        val session : Session? = sessionController.getSessionByID(sessionID)
        if (session != null)
            return ResponseEntity.ok(session)
        else
            return ResponseEntity.notFound().build()
    }

    @PutMapping("/{id}")
    fun updateSessionById(@PathVariable(value = "id") sessionID: Long, @Valid @RequestBody updatedSession: Session): ResponseEntity<Session> {
        val session : Session? = sessionController.updateSession(updatedSession)
        if (session != null)
            return ResponseEntity.ok(session)
        else
            return ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteSessionById(@PathVariable(value = "id") sessionID: Long): ResponseEntity<Void> {
        if (sessionController.deleteSession(sessionID))
            return ResponseEntity<Void>(HttpStatus.OK)
        else
            return ResponseEntity.notFound().build()
    }

    @PutMapping("/{sid}/participate/{uid}")
    fun setParticipation(@PathVariable(value = "sid") sessionID: Long, @PathVariable(value = "uid") userID: Long,
                         @Valid @RequestBody participates: Boolean): ResponseEntity<Void> {
        if (sessionController.setParticipation(sessionID, userID, participates))
            return ResponseEntity<Void>(HttpStatus.OK)
        else
            return ResponseEntity.notFound().build()
    }

    @PutMapping("/{sid}/report/{uid}")
    fun reportSessionField(@PathVariable(value = "sid") sessionID: Long, @PathVariable(value = "uid") reporterID: Long,
                           @Valid @RequestBody field: SessionField): ResponseEntity<Void> {
        if (sessionController.reportSessionField(sessionID, reporterID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }
}