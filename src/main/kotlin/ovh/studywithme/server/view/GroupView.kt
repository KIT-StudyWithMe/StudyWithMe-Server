package ovh.studywithme.server.view

import ovh.studywithme.server.controller.GroupController
import ovh.studywithme.server.controller.SessionController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ovh.studywithme.server.dao.UserDAO
import ovh.studywithme.server.dao.StudyGroupDAO
import ovh.studywithme.server.dao.SessionDAO
import ovh.studywithme.server.dao.StudyGroupMemberDAO
import ovh.studywithme.server.model.StudyGroupField
import javax.validation.Valid

/**
 * Group view
 *
 * @property groupController
 * @property sessionController
 * @constructor Create empty Group view
 */
@RestController
@RequestMapping("/groups")
class GroupView(
    private val groupController: GroupController, 
    private val sessionController: SessionController
    ) {

    /**
     * Get all groups
     *
     * @param query
     * @param lecture
     * @param name
     * @return
     */
    @GetMapping("")
    fun getAllGroups(@RequestParam("text") query: String?, @RequestParam("lecture") lecture: String?, 
            @RequestParam("name") name: String?): ResponseEntity<List<StudyGroupDAO>> {
        val results: List<StudyGroupDAO>
        if (query!=null) {
            results = groupController.searchGroup(query)
        } else if (lecture!=null){
            results = groupController.searchGroupByLecture(lecture)
        } else if (name!=null){
            results = groupController.searchGroupByName(name)
        } else {
            results = groupController.getAllGroups()
        }
        return ResponseEntity.ok(results)
    }

    /**
     * Create new group
     *
     * @param newGroup
     * @return
     */
    @PostMapping("/{userID}")
    fun createNewGroup(@Valid @RequestBody newGroup: StudyGroupDAO, @PathVariable(value = "userID") userID: Long): StudyGroupDAO =
        groupController.createGroup(newGroup, userID)

    /**
     * Get group by i d
     *
     * @param groupID
     * @return
     */
    @GetMapping("/{gid}")
    fun getGroupByID(@PathVariable(value = "gid") groupID: Long): ResponseEntity<StudyGroupDAO> {
        val group: StudyGroupDAO? = groupController.getGroupByID(groupID)
        if (group != null) {
            return ResponseEntity.ok(group)
        }
        else {
            return ResponseEntity.notFound().build()
        }
    }

    /**
     * Get group detail by i d
     *
     * @param groupID
     * @return
     */
    @GetMapping("/{gid}/detail")
    fun getGroupDetailByID(@PathVariable(value = "gid") groupID: Long): ResponseEntity<StudyGroupDAO> {
        val group: StudyGroupDAO? = groupController.getGroupByID(groupID)
        if (group != null) {
            return ResponseEntity.ok(group)
        }
        else {
            return ResponseEntity.notFound().build()
        }
    }

    /**
     * Get users in group
     *
     * @param groupID
     * @return
     */
    @GetMapping("/{gid}/users")
    fun getUsersInGroup(@PathVariable(value = "gid") groupID: Long): ResponseEntity<List<StudyGroupMemberDAO>> {
        val users: List<StudyGroupMemberDAO> = groupController.getUsersInGroup(groupID)
        return ResponseEntity.ok(users)
    }

    /**
     * Update group by id
     *
     * @param groupID
     * @param updatedGroup
     * @return
     */
    @PutMapping("/{gid}")
    fun updateGroupById(@PathVariable(value = "gid") groupID: Long, @Valid @RequestBody updatedGroup: StudyGroupDAO): ResponseEntity<StudyGroupDAO> {
        val group : StudyGroupDAO? = groupController.updateGroup(updatedGroup)
        if (group == null) {
            return ResponseEntity.notFound().build()
        }
        else {
            return ResponseEntity.ok(group)
        }
    }

    /**
     * Join group request
     *
     * @param groupID
     * @param userID
     * @return
     */
    @PutMapping("/{gid}/join/{uid}")
    fun joinGroupRequest(@PathVariable(value = "gid") groupID: Long, @PathVariable(value = "uid") userID: Long): ResponseEntity<Void> {
        if (groupController.joinGroupRequest(groupID, userID)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * Open group requests
     *
     * @param groupID
     * @return
     */
    @GetMapping("/{gid}/requests")
    fun openGroupRequests(@PathVariable(value = "gid") groupID: Long): ResponseEntity<List<UserDAO>> {
        val groupRequests : List<UserDAO> = groupController.getGroupRequests(groupID)
        return ResponseEntity.ok(groupRequests)
    }

    /**
     * Toggle group request
     *
     * @param groupID
     * @param userID
     * @param decision
     * @return
     */
    @PutMapping("/{gid}/users/{uid}/membership")
    fun toggleGroupRequest(@PathVariable(value = "gid") groupID: Long, @PathVariable(value = "uid") userID: Long,
                           @Valid @RequestBody decision: Boolean): ResponseEntity<Void> {
        if (groupController.toggleGroupMembership(groupID, userID, decision)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * Remove group user
     *
     * @param groupID
     * @param userID
     * @return
     */
    @DeleteMapping("/{gid}/users/{uid}")
    fun removeGroupUser(@PathVariable(value = "gid") groupID: Long, @PathVariable(value = "uid") userID: Long): ResponseEntity<Void> {
        if (groupController.deleteUserFromGroup(groupID, userID)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * Delete group
     *
     * @param groupID
     * @return
     */
    @DeleteMapping("/{gid}")
    fun deleteGroup(@PathVariable(value = "gid") groupID: Long): ResponseEntity<Void> {
        if (groupController.deleteGroup(groupID)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * Make user admin
     *
     * @param groupID
     * @param userID
     * @return
     */
    @PostMapping("/{gid}/users/{uid}/makeadmin")
    fun makeUserAdmin(@PathVariable(value = "gid") groupID: Long, @PathVariable(value = "uid") userID: Long): ResponseEntity<Void> {
        if (groupController.makeUserAdminInGroup(groupID, userID)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * Report group field
     *
     * @param groupID
     * @param reporterID
     * @param field
     * @return
     */
    @PutMapping("/{gid}/report/{uid}")
    fun reportGroupField(@PathVariable(value = "gid") groupID: Long, @PathVariable(value = "uid") reporterID: Long,
                         @Valid @RequestBody field: StudyGroupField): ResponseEntity<Void> {
        if (groupController.reportGroupField(groupID, reporterID, field)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * Get all group sessions
     *
     * @param groupID
     * @return
     */
    @GetMapping("/{gid}/sessions")
    fun getAllGroupSessions(@PathVariable(value = "gid") groupID: Long): ResponseEntity<List<SessionDAO>> {
        val sessions : List<SessionDAO> = sessionController.getAllGroupSessions(groupID)
        return ResponseEntity.ok(sessions)
    }

    /**
     * Create new session
     *
     * @param session
     * @return
     */
    @PostMapping("/{gid}/sessions")
    fun createNewSession(@Valid @RequestBody session: SessionDAO): SessionDAO =
        sessionController.createSession(session)
}