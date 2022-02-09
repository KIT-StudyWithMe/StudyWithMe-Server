package ovh.studywithme.server.view

import ovh.studywithme.server.controller.GroupController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ovh.studywithme.server.model.StudyGroup
import ovh.studywithme.server.model.User
import javax.validation.Valid

@RestController
@RequestMapping("/groups")
class GroupView(private val groupController: GroupController) {

    @GetMapping("")
    fun getAllGroups(): ResponseEntity<List<StudyGroup>> {
        val groups: List<StudyGroup> = groupController.getAllGroups()
        if (groups.isNotEmpty()) {
            return ResponseEntity.ok(groups)
        }
        else {
            return ResponseEntity.notFound().build()
        }
    }

    @PostMapping("")
    fun createNewGroup(@Valid @RequestBody newGroup: StudyGroup): StudyGroup =
        groupController.createGroup(newGroup)

    @GetMapping("")
    fun searchGroups(@RequestParam("text") query: String): ResponseEntity<List<StudyGroup>> {
        val groups : List<StudyGroup> =  groupController.searchGroup(query)
        if (groups.isNotEmpty())
            return ResponseEntity.ok(groups)
        else
            return ResponseEntity.notFound().build()
    }

    @GetMapping("")
    fun searchGroupsByLecture(@RequestParam("lecture") lecture: String): ResponseEntity<List<StudyGroup>> {
        val groups : List<StudyGroup> =  groupController.searchGroupByLecture(lecture)
        if (groups.isNotEmpty())
            return ResponseEntity.ok(groups)
        else
            return ResponseEntity.notFound().build()
    }

    @GetMapping("")
    fun searchGroupsByName(@RequestParam("name") name: String): ResponseEntity<List<StudyGroup>> {
        val groups : List<StudyGroup> =  groupController.searchGroupByLecture(name)
        if (groups.isNotEmpty())
            return ResponseEntity.ok(groups)
        else
            return ResponseEntity.notFound().build()
    }

    @GetMapping("/{id}")
    fun getGroupByID(@PathVariable(value = "id") groupID: Long): ResponseEntity<StudyGroup> {
        val group: StudyGroup? = groupController.getGroupByID(groupID)
        if (group != null) {
            return ResponseEntity.ok(group)
        }
        else {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{id}/users")
    fun getUsersInGroup(@PathVariable(value = "id") groupID: Long): ResponseEntity<List<User>> {
        val users: List<User> = groupController.getUsersInGroup(groupID)
        if (users.isNotEmpty()) {
            return ResponseEntity.ok(users)
        }
        else {
            return ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}/detail")
    fun updateUserById(@PathVariable(value = "id") groupID: Long, @Valid @RequestBody updatedGroup: StudyGroup): ResponseEntity<StudyGroup> {
        val group : StudyGroup? = groupController.updateGroup(updatedGroup)
        if (group == null) {
            return ResponseEntity.notFound().build()
        }
        else {
            return ResponseEntity.ok(group)

        }
    }

    @PutMapping("/{id}/join/{id}")
    fun joinGroupRequest(@PathVariable(value = "id") groupID: Long, @PathVariable(value = "id") userID: Long): ResponseEntity<Void> {
        if (groupController.joinGroupRequest(groupID, userID)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }
}