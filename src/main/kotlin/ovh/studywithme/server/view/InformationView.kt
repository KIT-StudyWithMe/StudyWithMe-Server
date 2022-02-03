package ovh.studywithme.server.view

import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.controller.InformationController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("")
class InformationView(private val informationController: InformationController) {

    @GetMapping("/institutions")
    fun getAllUsers(@RequestParam("name") institutionName: String?): ResponseEntity<List<Institution>> {
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

    @PostMapping("/institutions")
    fun createNewInstitution(@Valid @RequestBody institution: Institution): Institution =
        informationController.createNewInstitution(institution)

    @DeleteMapping("/institutions/{id}")
    fun deleteUserById(@PathVariable(value = "id") institutionID: Long): ResponseEntity<Void> {
        if(informationController.deleteInstitution(institutionID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }
}