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
 * Information view
 *
 * @property informationController
 * @constructor Create empty Information view
 */
@RestController
@RequestMapping("")
class InformationView(private val informationController: InformationController) {

    /**
     * Get all institutions
     *
     * @param institutionName
     * @return
     */
    @GetMapping("/institutions")
    fun getAllInstitutions(@RequestParam("name") institutionName: String?): ResponseEntity<List<InstitutionDAO>> {
        if (institutionName == null) {
            return ResponseEntity.ok(informationController.getAllInstitutions())
        }
        else {
            val institutions : List<InstitutionDAO> =  informationController.getInstitutionsByName(institutionName)
            if (!institutions.isEmpty())
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
     * @param majorID
     * @param lectureID
     * @return
     */
    @GetMapping("/majors/{id}/lectures/{lid}")
    fun getLectureById(@PathVariable(value = "id") majorID: Long, @PathVariable(value = "lid") lectureID: Long): ResponseEntity<LectureDAO> {
        val lecture : LectureDAO? = informationController.getLectureByID(majorID, lectureID)
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