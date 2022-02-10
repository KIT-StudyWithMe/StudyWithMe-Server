package ovh.studywithme.server.view

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ovh.studywithme.server.controller.ReportController
import ovh.studywithme.server.model.UserField
import ovh.studywithme.server.model.StudyGroupField
import ovh.studywithme.server.model.SessionField
import ovh.studywithme.server.dao.StudyGroupReportDAO
import ovh.studywithme.server.dao.UserReportDAO
import ovh.studywithme.server.dao.SessionReportDAO


/**
 * Report view
 *
 * @property reportController
 * @constructor Create empty Report view
 */
@RestController
@RequestMapping("/reports")
class ReportView(private val reportController: ReportController) {

    /**
     * Get all group reports
     *
     * @return
     */
    @GetMapping("/group")
    fun getAllGroupReports(): ResponseEntity<List<StudyGroupReportDAO>> {

        val reports: List<StudyGroupReportDAO> = reportController.getAllGroupReports()
        if (!reports.isEmpty())
            return ResponseEntity.ok(reports)
        else
            return ResponseEntity.notFound().build()
    }

    /**
     * Get all user reports
     *
     * @return
     */
    @GetMapping("/user")
    fun getAllUserReports(): ResponseEntity<List<UserReportDAO>> {

        val reports: List<UserReportDAO> = reportController.getAllUserReports()
        if (!reports.isEmpty())
            return ResponseEntity.ok(reports)
        else
            return ResponseEntity.notFound().build()
    }

    /**
     * Get all session reports
     *
     * @return
     */
    @GetMapping("/session")
    fun getAllSessionReports(): ResponseEntity<List<SessionReportDAO>> {

        val reports: List<SessionReportDAO> = reportController.getAllSessionReports()
        if (!reports.isEmpty())
            return ResponseEntity.ok(reports)
        else
            return ResponseEntity.notFound().build()
    }

    /**
     * Delete group report
     *
     * @param reporterID
     * @param groupID
     * @param field
     * @return
     */
    @GetMapping("/group/{rid}/{gid}/{fid}")
    fun deleteGroupReport(@PathVariable(value = "rid") reporterID: Long, @PathVariable(value = "gid") groupID: Long,
                          @PathVariable(value = "fid") field: StudyGroupField): ResponseEntity<Void> {

        if (reportController.deleteGroupReport(reporterID, groupID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * Delete user report
     *
     * @param reporterID
     * @param userID
     * @param field
     * @return
     */
    @GetMapping("/group/{rid}/{uid}/{fid}")
    fun deleteUserReport(@PathVariable(value = "rid") reporterID: Long, @PathVariable(value = "uid") userID: Long,
                          @PathVariable(value = "fid") field: UserField): ResponseEntity<Void> {

        if (reportController.deleteUserReport(reporterID, userID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * Delete session report
     *
     * @param reporterID
     * @param sessionID
     * @param field
     * @return
     */
    @GetMapping("/group/{rid}/{sid}/{fid}")
    fun deleteSessionReport(@PathVariable(value = "rid") reporterID: Long, @PathVariable(value = "sid") sessionID: Long,
                          @PathVariable(value = "fid") field: SessionField): ResponseEntity<Void> {

        if (reportController.deleteSessionReport(reporterID, sessionID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }
}