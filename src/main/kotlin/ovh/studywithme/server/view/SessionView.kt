import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ovh.studywithme.server.controller.SessionController
import ovh.studywithme.server.model.Session
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

    @PutMapping("/{id}/participate")
    fun setParticipation(@PathVariable(value = "id") sessionID: Long): ResponseEntity<Void> {
        // TODO How to get userID and participates?
        if (sessionController.setParticipation(sessionID, 0, true))
            return ResponseEntity<Void>(HttpStatus.OK)
        else
            return ResponseEntity.notFound().build()
    }
}