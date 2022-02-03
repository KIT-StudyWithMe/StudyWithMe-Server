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
            return ResponseEntity.ok(informationController.getAllInstitutions()) //TODO
        }
        else {
            val institutions : List<Institution> =  informationController.getUserByFUID(institutionName)
            if (!institutions.isEmpty())
                return ResponseEntity.ok(institutions)
            else
                return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("")
    fun createNewUser(@Valid @RequestBody user: User): User =
        userController.createUser(user)

    @GetMapping("/{id}")
    fun getUserById(@PathVariable(value = "id") userID: Long): ResponseEntity<User> {
        val user : User? = userController.getUserByID(userID)
        if (user != null) return ResponseEntity.ok(user)
        return ResponseEntity.notFound().build()
    }

    @PutMapping("/{id}")
    fun updateUserById(@PathVariable(value = "id") userID: Long,
                          @Valid @RequestBody newUser: User): ResponseEntity<User> {
        val user : User? = userController.updateUser(userID, newUser)
        if (user == null) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable(value = "id") userID: Long): ResponseEntity<Void> {
        if(userController.deleteUser(userID)) return ResponseEntity<Void>(HttpStatus.OK) 
        return ResponseEntity.notFound().build()
    }
}