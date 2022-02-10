package ovh.studywithme.server.view

import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.Major
import ovh.studywithme.server.model.Lecture
import ovh.studywithme.server.controller.InformationController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("")
class InformationView(private val informationController: InformationController) {

    @GetMapping("/institutions")
    fun getAllInstitutions(@RequestParam("name") institutionName: String?): ResponseEntity<List<Institution>> {
        if (institutionName == null) {
            return ResponseEntity.ok(informationController.getAllInstitutions())
        }
        else {
            val institutions : List<Institution> =  informationController.getInstitutionsByName(institutionName)
            if (!institutions.isEmpty())
                return ResponseEntity.ok(institutions)
            else
                return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/institutions/{id}")
    fun getInstitutionById(@PathVariable(value = "id") institutionID: Long): ResponseEntity<Institution> {
        val institution : Institution? = informationController.getInstitutionByID(institutionID)
        if (institution != null) return ResponseEntity.ok(institution)
        return ResponseEntity.notFound().build()
    }

    @PostMapping("/institutions")
    fun createNewInstitution(@Valid @RequestBody institution: Institution): Institution =
        informationController.createNewInstitution(institution)

    @DeleteMapping("/institutions/{id}")
    fun deleteInstitutionById(@PathVariable(value = "id") institutionID: Long): ResponseEntity<Void> {
        if(informationController.deleteInstitution(institutionID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }

    @GetMapping("/majors/{id}")
    fun getMajorById(@PathVariable(value = "id") majorID: Long): ResponseEntity<Major> {
        val major : Major? = informationController.getMajorByID(majorID)
        if (major != null) return ResponseEntity.ok(major)
        return ResponseEntity.notFound().build()
    }

    @GetMapping("/majors")
    fun getAllMajors(@RequestParam("name") majorName: String?): ResponseEntity<List<Major>> {
        if (majorName == null) {
            return ResponseEntity.ok(informationController.getAllMajors())
        }
        else {
            val majors : List<Major> =  informationController.getMajorsByName(majorName)
            if (!majors.isEmpty())
                return ResponseEntity.ok(majors)
            else
                return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/majors")
    fun createNewMajor(@Valid @RequestBody major: Major): Major =
        informationController.createNewMajor(major)

    @DeleteMapping("/majors/{id}")
    fun deleteMajorById(@PathVariable(value = "id") majorID: Long): ResponseEntity<Void> {
        if(informationController.deleteMajor(majorID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }

    @GetMapping("/majors/{id}/lectures/{lid}")
    fun getLectureById(@PathVariable(value = "id") majorID: Long, @PathVariable(value = "lid") lectureID: Long): ResponseEntity<Lecture> {
        val lecture : Lecture? = informationController.getLectureByID(majorID, lectureID)
        if (lecture != null) return ResponseEntity.ok(lecture)
        return ResponseEntity.notFound().build()
    }

    @GetMapping("/majors/{id}/lectures")
    fun getAllLectures(@PathVariable(value = "id") majorID: Long, @RequestParam("name") lectureName: String?): ResponseEntity<List<Lecture>> {
        if (lectureName == null) {
            return ResponseEntity.ok(informationController.getAllLectures(majorID))
        }
        else {
            val lectures : List<Lecture> =  informationController.getLecturesByName(majorID, lectureName)
            if (!lectures.isEmpty())
                return ResponseEntity.ok(lectures)
            else
                return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/majors/{id}/lectures")
    fun createNewLecture(@PathVariable(value = "id") majorID: Long, @Valid @RequestBody lecture: Lecture): Lecture =
        informationController.createNewLecture(majorID, lecture)

    @DeleteMapping("/majors/{id}/lectures/{lid}")
    fun deleteLectureById(@PathVariable(value = "id") majorID: Long, @PathVariable(value = "lid") lectureID: Long): ResponseEntity<Void> {
        if(informationController.deleteLecture(majorID, lectureID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }

}