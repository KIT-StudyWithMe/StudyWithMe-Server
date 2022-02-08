package ovh.studywithme.server.view

import ovh.studywithme.server.controller.GroupController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/groups")
class GroupView(private val groupController: GroupController) {

}