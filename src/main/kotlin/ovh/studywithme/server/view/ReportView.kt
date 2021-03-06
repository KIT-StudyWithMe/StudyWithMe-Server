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
 * The report view is exposed to the client. It is the required way for the client to communicate with the server.
 * All rest-endpoints are defined here and only data access objects are expected and returned.
 * Spring auto-creates a thread for every request and calls the corresponding method.
 *
 * This class bundles all functionality related to reports.
 * For data exchange between server and client data access objects must be used.
 *
 * @property reportController The server's internal report management logic that the view uses to process the client's requests.
 * @constructor Create a report view, all variables are instanced by Spring's autowire.
 */
@RestController
@RequestMapping("/reports")
class ReportView(private val reportController: ReportController) {

    /**
     * This method is executed when GET /reports/group is called.
     *
     * To process reports regarding inappropriate content of group details, a moderator must be able to retrieve a list
     * of such reports.
     * The list is returned here so the requesting moderator can look into these reports and then take appropriate actions.
     *
     * @return A list containing all group reports together with http status "200: OK".
     */
    @GetMapping("/group")
    fun getAllGroupReports(): ResponseEntity<List<StudyGroupReportDAO>> {
        return ResponseEntity.ok(reportController.getAllGroupReports())
    }

    /**
     * This method is executed when GET /reports/user is called.
     *
     * To process reports regarding inappropriate content of user details, a moderator must be able to retrieve a list
     * of such reports.
     * The list is returned here so the requesting moderator can look into these reports and then take appropriate actions.
     *
     * @return A list containing all user reports together with http status "200: OK".
     */
    @GetMapping("/user")
    fun getAllUserReports(): ResponseEntity<List<UserReportDAO>> {
            return ResponseEntity.ok(reportController.getAllUserReports())
    }

    /**
     * This method is executed when GET /reports/session is called.
     *
     * To process reports regarding inappropriate content of session details, a moderator must be able to retrieve a list
     * of such reports.
     * The list is returned here so the requesting moderator can look into these reports and then take appropriate actions.
     *
     * @return A list containing all session reports together with http status "200: OK".
     */
    @GetMapping("/session")
    fun getAllSessionReports(): ResponseEntity<List<SessionReportDAO>> {
            return ResponseEntity.ok(reportController.getAllSessionReports())
    }

    /**
     * This method is executed when DELETE /reports/group/{reporterID}/{groupID}/{fieldName} is called.
     *
     * When a moderator looked into a report and took the appropriate action, the report needs to be deleted.
     * The client sends all data that is needed to identify the report, then it's deleted.
     *
     * @param reporterID The reporting user's unique identifier.
     * @param groupID The reported group's unique identifier.
     * @param field The name of the exact field that was reported in a group's details.
     * @return http status "200: OK" if the report was successfully deleted and http status "404: NOT FOUND" otherwise.
     */
    @DeleteMapping("/group/{rid}/{gid}/{fid}")
    fun deleteGroupReport(@PathVariable(value = "rid") reporterID: Long, @PathVariable(value = "gid") groupID: Long,
                          @PathVariable(value = "fid") field: StudyGroupField): ResponseEntity<Void> {

        if (reportController.deleteGroupReport(reporterID, groupID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when DELETE /reports/user/{reporterID}/{userID}/{fieldName} is called.
     *
     * When a moderator looked into a report and took the appropriate action, the report needs to be deleted.
     * The client sends all data that is needed to identify the report, then it's deleted.
     *
     * @param reporterID The reporting user's unique identifier.
     * @param userID The reported user's unique identifier.
     * @param field The name of the exact field that was reported in a user's details.
     * @return http status "200: OK" if the report was successfully deleted and http status "404: NOT FOUND" otherwise.
     */
    @DeleteMapping("/user/{rid}/{uid}/{fid}")
    fun deleteUserReport(@PathVariable(value = "rid") reporterID: Long, @PathVariable(value = "uid") userID: Long,
                          @PathVariable(value = "fid") field: UserField): ResponseEntity<Void> {

        if (reportController.deleteUserReport(reporterID, userID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when DELETE /reports/session/{reporterID}/{sessionID}/{fieldName} is called.
     *
     * When a moderator looked into a report and took the appropriate action, the report needs to be deleted.
     * The client sends all data that is needed to identify the report, then it's deleted.
     *
     * @param reporterID The reporting user's unique identifier.
     * @param sessionID The reported session's unique identifier.
     * @param field The name of the exact field that was reported in a session's details.
     * @return http status "200: OK" if the report was successfully deleted and http status "404: NOT FOUND" otherwise.
     */
    @DeleteMapping("/session/{rid}/{sid}/{fid}")
    fun deleteSessionReport(@PathVariable(value = "rid") reporterID: Long, @PathVariable(value = "sid") sessionID: Long,
                          @PathVariable(value = "fid") field: SessionField): ResponseEntity<Void> {

        if (reportController.deleteSessionReport(reporterID, sessionID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }
}