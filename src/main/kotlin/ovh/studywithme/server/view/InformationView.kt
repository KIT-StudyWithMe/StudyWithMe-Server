package ovh.studywithme.server.view

import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.controller.InformationController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping("")
class InformationView(private val informationController: InformationController) {

    @GetMapping("/getUserID")
    fun getVersion(user: Principal): ResponseEntity<String> {
        return ResponseEntity.ok(user.toString())
    }

    @RequestMapping("/user")
    fun user() : String {
        //LinkedHashMap<String, Object> details = (LinkedHashMap<String, Object>) authentication.getUserAuthentication().getDetails()
        //return details.get("email")
        
        return SecurityContextHolder.getContext().getAuthentication().toString()
    }

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

    @PostMapping("/institutions")
    fun createNewInstitution(@Valid @RequestBody institution: Institution): Institution =
        informationController.createNewInstitution(institution)

    @DeleteMapping("/institutions/{id}")
    fun deleteInstitutionByID(@PathVariable(value = "id") institutionID: Long): ResponseEntity<Void> {
        if(informationController.deleteInstitution(institutionID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }
}