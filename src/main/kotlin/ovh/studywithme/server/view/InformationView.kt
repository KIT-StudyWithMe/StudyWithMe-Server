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
 * This class bundles all functionality related to where and what a user studies.
 * Spring auto-creates a thread for every request and calls the corresponding method.
 *
 * @property informationController The server's internal information management logic that the view uses to process the client's requests.
 * @constructor Create an information view, all variables are instanced by Spring's autowire.
 */
@RestController
@RequestMapping("")
class InformationView(private val informationController: InformationController) {

    /**
     * This method is executed when GET /institutions is called. It returns a list of all institutions.
     *
     * @param institutionName The institution's name.
     * @return
     */
    @GetMapping("/institutions")
    fun getAllInstitutions(@RequestParam("name") institutionName: String?): ResponseEntity<List<InstitutionDAO>> {
        if (institutionName == null) {
            return ResponseEntity.ok(informationController.getAllInstitutions())
        }
        else {
            val institutions : List<InstitutionDAO> =  informationController.getInstitutionsByName(institutionName)
            if (institutions.isNotEmpty())
                return ResponseEntity.ok(institutions)
            else
                return ResponseEntity.notFound().build()
        }
    }

    /**
     * Get institution by id
     *
     * @param institutionID
     * @return
     */
    @GetMapping("/institutions/{id}")
    fun getInstitutionById(@PathVariable(value = "id") institutionID: Long): ResponseEntity<InstitutionDAO> {
        val institution : InstitutionDAO? = informationController.getInstitutionByID(institutionID)
        if (institution != null) return ResponseEntity.ok(institution)
        return ResponseEntity.notFound().build()
    }

    /**
     * Create new institution
     *
     * @param institution
     * @return
     */
    @PostMapping("/institutions")
    fun createNewInstitution(@Valid @RequestBody institution: InstitutionDAO): InstitutionDAO =
        informationController.createNewInstitution(institution)

    /**
     * Delete institution by id
     *
     * @param institutionID
     * @return
     */
    @DeleteMapping("/institutions/{id}")
    fun deleteInstitutionById(@PathVariable(value = "id") institutionID: Long): ResponseEntity<Void> {
        if(informationController.deleteInstitution(institutionID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }

    /**
     * Get major by id
     *
     * @param majorID
     * @return
     */
    @GetMapping("/majors/{id}")
    fun getMajorById(@PathVariable(value = "id") majorID: Long): ResponseEntity<MajorDAO> {
        val major : MajorDAO? = informationController.getMajorByID(majorID)
        if (major != null) return ResponseEntity.ok(major)
        return ResponseEntity.notFound().build()
    }

    /**
     * Get all majors
     *
     * @param majorName
     * @return
     */
    @GetMapping("/majors")
    fun getAllMajors(@RequestParam("name") majorName: String?): ResponseEntity<List<MajorDAO>> {
        if (majorName == null) {
            return ResponseEntity.ok(informationController.getAllMajors())
        }
        else {
            val majors : List<MajorDAO> =  informationController.getMajorsByName(majorName)
            if (!majors.isEmpty())
                return ResponseEntity.ok(majors)
            else
                return ResponseEntity.notFound().build()
        }
    }

    /**
     * Create new major
     *
     * @param major
     * @return
     */
    @PostMapping("/majors")
    fun createNewMajor(@Valid @RequestBody major: MajorDAO): MajorDAO =
        informationController.createNewMajor(major)

    /**
     * Delete major by id
     *
     * @param majorID
     * @return
     */
    @DeleteMapping("/majors/{id}")
    fun deleteMajorById(@PathVariable(value = "id") majorID: Long): ResponseEntity<Void> {
        if(informationController.deleteMajor(majorID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }

    /**
     * Get lecture by id
     *
     * @param lectureID
     * @return
     */
    @GetMapping("/lectures/{lid}")
    fun getLectureById(@PathVariable(value = "lid") lectureID: Long): ResponseEntity<LectureDAO> {
        val lecture : LectureDAO? = informationController.getLectureByID(lectureID)
        if (lecture != null) return ResponseEntity.ok(lecture)
        return ResponseEntity.notFound().build()
    }

    /**
     * Get all lectures
     *
     * @param majorID
     * @param lectureName
     * @return
     */
    @GetMapping("/majors/{id}/lectures")
    fun getAllLectures(@PathVariable(value = "id") majorID: Long, @RequestParam("name") lectureName: String?): ResponseEntity<List<LectureDAO>> {
        if (lectureName == null) {
            return ResponseEntity.ok(informationController.getAllLectures(majorID))
        }
        else {
            val lectures : List<LectureDAO> =  informationController.getLecturesByName(majorID, lectureName)
            if (!lectures.isEmpty())
                return ResponseEntity.ok(lectures)
            else
                return ResponseEntity.notFound().build()
        }
    }

    /**
     * Create new lecture
     *
     * @param majorID
     * @param lecture
     * @return
     */
    @PostMapping("/majors/{id}/lectures")
    fun createNewLecture(@PathVariable(value = "id") majorID: Long, @Valid @RequestBody lecture: LectureDAO): LectureDAO =
        informationController.createNewLecture(majorID, lecture)

    /**
     * Delete lecture by id
     *
     * @param majorID
     * @param lectureID
     * @return
     */
    @DeleteMapping("/majors/{id}/lectures/{lid}")
    fun deleteLectureById(@PathVariable(value = "id") majorID: Long, @PathVariable(value = "lid") lectureID: Long): ResponseEntity<Void> {
        if(informationController.deleteLecture(majorID, lectureID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }

}