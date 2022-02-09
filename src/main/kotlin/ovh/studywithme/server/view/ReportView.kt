import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ovh.studywithme.server.controller.ReportController
import ovh.studywithme.server.model.*

@RestController
@RequestMapping("/reports")
class ReportView(private val reportController: ReportController) {

    @GetMapping("/group")
    fun getAllGroupReports(): ResponseEntity<List<StudyGroupReport>> {

        val reports: List<StudyGroupReport> = reportController.getAllGroupReports()
        if (!reports.isEmpty())
            return ResponseEntity.ok(reports)
        else
            return ResponseEntity.notFound().build()
    }

    @GetMapping("/user")
    fun getAllUserReports(): ResponseEntity<List<UserReport>> {

        val reports: List<UserReport> = reportController.getAllUserReports()
        if (!reports.isEmpty())
            return ResponseEntity.ok(reports)
        else
            return ResponseEntity.notFound().build()
    }

    @GetMapping("/session")
    fun getAllSessionReports(): ResponseEntity<List<SessionReport>> {

        val reports: List<SessionReport> = reportController.getAllSessionReports()
        if (!reports.isEmpty())
            return ResponseEntity.ok(reports)
        else
            return ResponseEntity.notFound().build()
    }

    @GetMapping("/group/{rid}/{gid}/{fid}")
    fun deleteGroupReport(@PathVariable(value = "rid") reporterID: Long, @PathVariable(value = "gid") groupID: Long,
                          @PathVariable(value = "fid") field: StudyGroupField): ResponseEntity<Void> {

        if (reportController.deleteGroupReport(reporterID, groupID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    @GetMapping("/group/{rid}/{uid}/{fid}")
    fun deleteUserReport(@PathVariable(value = "rid") reporterID: Long, @PathVariable(value = "uid") userID: Long,
                          @PathVariable(value = "fid") field: UserField): ResponseEntity<Void> {

        if (reportController.deleteUserReport(reporterID, userID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    @GetMapping("/group/{rid}/{sid}/{fid}")
    fun deleteSessionReport(@PathVariable(value = "rid") reporterID: Long, @PathVariable(value = "sid") sessionID: Long,
                          @PathVariable(value = "fid") field: SessionField): ResponseEntity<Void> {

        if (reportController.deleteSessionReport(reporterID, sessionID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }
}