package ovh.studywithme.server.view

import ovh.studywithme.server.controller.InformationController
import ovh.studywithme.server.dao.InstitutionDAO
import ovh.studywithme.server.dao.MajorDAO
import ovh.studywithme.server.dao.LectureDAO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * The information view is exposed to the client. It is the required way for the client to communicate with the server.
 * All rest-endpoints are defined here and only data access objects are expected and returned.
 * Spring auto-creates a thread for every request and calls the corresponding method.
 *
 * This class bundles all functionality related to where and what a user studies.
 * For data exchange between server and client data access objects must be used.
 *
 * @property informationController The server's internal information management logic that the view uses to process the client's requests.
 * @constructor Create an information view, all variables are instanced by Spring's autowire.
 */
@RestController
@RequestMapping("")
class InformationView(private val informationController: InformationController) {

    /**
     * This method is executed when GET /institutions is called.
     *
     * It returns a list of institutions.
     * If the requested parameter institutionName is set, a list of institutions whose name starts with
     * the parameter's value, which is a string, is returned.
     * If the parameter institutionName is not set, a list of all institution on the server is returned.
     *
     * @param institutionName The institution's name.
     * @return A list of institutions.
     */
    @GetMapping("/institutions")
    fun getAllInstitutions(@RequestParam("name") institutionName: String?): ResponseEntity<List<InstitutionDAO>> {
        if (institutionName == null) {
            return ResponseEntity.ok(informationController.getAllInstitutions())
        }
        else {
            return ResponseEntity.ok(informationController.getInstitutionsByName(institutionName))
        }
    }

    /**
     * This method is executed when GET /institutions/{id} is called.
     *
     * Gets all information about an institution by its id.
     * With the result http status "200: OK" is sent if an institution with the given id was found
     * and http status "404: NOT FOUND" otherwise.
     *
     * @param institutionID The institutions unique id.
     * @return The requested institution if it was found.
     */
    @GetMapping("/institutions/{id}")
    fun getInstitutionById(@PathVariable(value = "id") institutionID: Long): ResponseEntity<InstitutionDAO> {
        val institution : InstitutionDAO? = informationController.getInstitutionByID(institutionID)
        if (institution != null) return ResponseEntity.ok(institution)
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when POST /institutions is called.
     *
     * It's used when a user signs up with an institution in his details that is not in the system yet.
     *
     * @param institution Institution information with which the new institution should be created.
     * @return The institution that was created on the server, including its assigned institutionID, and http status "200: OK".
     */
    @PostMapping("/institutions")
    fun createNewInstitution(@Valid @RequestBody institution: InstitutionDAO): ResponseEntity<InstitutionDAO> =
        ResponseEntity.ok(informationController.createNewInstitution(institution))

    /**
     * This method is executed when DELETE /institutions/{id} is called.
     *
     * Deletes an institution from the server.
     * It's identified by its id.
     * A http status as return value indicates if the operation was successful.
     *
     * @param institutionID
     * @return http status "200: OK" if the institution was successfully deleted and http status "404: NOT FOUND" otherwise.
     */
    @DeleteMapping("/institutions/{id}")
    fun deleteInstitutionById(@PathVariable(value = "id") institutionID: Long): ResponseEntity<Void> {
        if(informationController.deleteInstitution(institutionID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when GET /majors/{id} is called.
     *
     * Gets all information about a major by its id.
     * With the result http status "200: OK" is sent if a major with the given id was found
     * and http status "404: NOT FOUND" otherwise.
     *
     * @param majorID The major's unique id.
     * @return The requested major if it was found.
     */
    @GetMapping("/majors/{id}")
    fun getMajorById(@PathVariable(value = "id") majorID: Long): ResponseEntity<MajorDAO> {
        val major : MajorDAO? = informationController.getMajorByID(majorID)
        if (major != null) return ResponseEntity.ok(major)
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when GET /majors is called.
     *
     * It returns a list of majors.
     * If the requested parameter majorName is set, a list of majors whose name starts with
     * the parameter's value, which is a string, is returned.
     * If the parameter majorName is not set, a list of all majors on the server is returned.
     *
     * @param majorName The major's name.
     * @return A list of majors.
     */
    @GetMapping("/majors")
    fun getAllMajors(@RequestParam("name") majorName: String?): ResponseEntity<List<MajorDAO>> {
        if (majorName == null) {
            return ResponseEntity.ok(informationController.getAllMajors())
        }
        else {
            return ResponseEntity.ok(informationController.getMajorsByName(majorName))
        }
    }

    /**
     * This method is executed when POST /majors is called.
     *
     * It's used when a user signs up with a major in his details which is not in the system yet.
     *
     * @param major Major information with which the new major should be created.
     * @return The major that was created on the server, including its assigned majorID, and http status "200: OK".
     */
    @PostMapping("/majors")
    fun createNewMajor(@Valid @RequestBody major: MajorDAO): ResponseEntity<MajorDAO> =
        ResponseEntity.ok(informationController.createNewMajor(major))

    /**
     * This method is executed when DELETE /majors/{id} is called.
     *
     * Deletes a major from the server.
     * It's identified by its id.
     * A http status as return value indicates if the operation was successful.
     *
     * @param majorID The major's unique identifier.
     * @return http status "200: OK" if the major was successfully deleted and http status "404: NOT FOUND" otherwise.
     */
    @DeleteMapping("/majors/{id}")
    fun deleteMajorById(@PathVariable(value = "id") majorID: Long): ResponseEntity<Void> {
        if(informationController.deleteMajor(majorID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when GET /lectures/{id} is called.
     *
     * Gets all information about a lecture by its id.
     * With the result http status "200: OK" is sent if a lecture with the given id was found
     * and http status "404: NOT FOUND" otherwise.
     *
     * @param lectureID The lecture's unique id.
     * @return The requested lecture if it was found.
     */
    @GetMapping("/lectures/{lid}")
    fun getLectureById(@PathVariable(value = "lid") lectureID: Long): ResponseEntity<LectureDAO> {
        val lecture : LectureDAO? = informationController.getLectureByID(lectureID)
        if (lecture != null) return ResponseEntity.ok(lecture)
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when GET /majors/{id}/lectures is called.
     *
     * It returns a list of lectures.
     * If the requested parameter lectureName is set, a list of lectures whose name starts with
     * the parameter's value, which is a string, is returned.
     * If the parameter lectureName is not set, a list of all lectures on the server is returned.
     *
     * @param majorID The major's unique identifier.
     * @param lectureName The lecture's name.
     * @return A list of lectures.
     */
    @GetMapping("/majors/{id}/lectures")
    fun getAllLectures(@PathVariable(value = "id") majorID: Long, @RequestParam("name") lectureName: String?): ResponseEntity<List<LectureDAO>> {
        if (lectureName == null) {
            return ResponseEntity.ok(informationController.getAllLectures(majorID))
        }
        else {
            return ResponseEntity.ok(informationController.getLecturesByName(majorID, lectureName))
        }
    }

    /**
     * This method is executed when POST /majors/{id}/lectures is called.
     *
     * It's used when a user signs up with a lecture in his details which is not in the system for his major yet.
     * The lecture is created for a certain major.
     *
     * @param majorID The major's unique identifier.
     * @param lecture Lecture information with which the new major should be created.
     * @return The lecture that was created on the server, including its assigned lectureID, and http status "200: OK".
     */
    @PostMapping("/majors/{id}/lectures")
    fun createNewLecture(@PathVariable(value = "id") majorID: Long, @Valid @RequestBody lecture: LectureDAO): ResponseEntity<LectureDAO> =
        ResponseEntity.ok(informationController.createNewLecture(majorID, lecture))

    /**
     * This method is executed when DELETE /majors/{id}/lectures/{id} is called.
     *
     * Deletes a lecture from the server.
     * It's identified by its id and only deleted for a certain major, not all majors.
     * A http status as return value indicates if the operation was successful.
     *
     * @param majorID The major's unique identifier.
     * @param lectureID The lecture's unique identifier.
     * @return http status "200: OK" if the major was successfully deleted and http status "404: NOT FOUND" otherwise.
     */
    @DeleteMapping("/majors/{id}/lectures/{lid}")
    fun deleteLectureById(@PathVariable(value = "id") majorID: Long, @PathVariable(value = "lid") lectureID: Long): ResponseEntity<Void> {
        if(informationController.deleteLecture(majorID, lectureID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }
}