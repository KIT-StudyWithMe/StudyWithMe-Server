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
 * The group view is exposed to the client. It is the required way for the client to communicate with the server.
 * All rest-endpoints are defined here and only data access objects are expected and returned.
 * Spring auto-creates a thread for every request and calls the corresponding method.
 *
 * This class bundles all functionality related to groups.
 * For data exchange between server and client data access objects must be used.
 *
 * @property groupController The server's internal group management logic that the view uses to process the client's requests.
 * @property sessionController The server's internal session management logic that the view uses to process the client's requests.
 * @constructor Create a group view, all variables are instanced by Spring's autowire.
 */
@RestController
@RequestMapping("/groups")
class GroupView(
    private val groupController: GroupController, 
    private val sessionController: SessionController
    ) {

    /**
     * This method returns a set of groups. If no or invalid parameters are specified, all groups are returned.
     * If lecture is specified it searches for groups with that lecture.
     * If name is specified it searches for groups with that name.
     * If text is specified it searches for groups for a lecture with that text or a group with that text as name.
     *
     * @param query Search for either lecture-name or group-name
     * @param lecture Search groups for this lecture-name
     * @param name Search groups with this group-name
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
     * This method is executed when POST /groups/{id} is called.
     *
     * Creates a new group. Used when a user created a new study-group.
     * Use 0 as groupID for new groups here.
     * The id it got from the server will be returned with the return object.
     *
     * @param newGroup Group information with which the new group should be created.
     * @return The group that was created on the server, including its correct groupID.
     */
    @PostMapping("/{userID}")
    fun createNewGroup(@Valid @RequestBody newGroup: StudyGroupDAO, @PathVariable(value = "userID") userID: Long): ResponseEntity<StudyGroupDAO> =
        ResponseEntity.ok(groupController.createGroup(newGroup, userID))

    /**
     * This method is executed when GET /groups/{id} is called.
     *
     * Gets a group's restricted information by its id.
     *
     * @param groupID The group's unique identifier.
     * @return http status "200: OK" if the group was found together with the group's information and http status "404: NOT FOUND" otherwise.
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
     * This method is executed when GET /groups/{id}/detail is called.
     *
     * Gets a group's complete information by its id.
     *
     * @param groupID The group's unique identifier.
     * @return http status "200: OK" if the group was found together with the group's information and http status "404: NOT FOUND" otherwise.
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
     * This method is executed when GET /groups/{id}/users is called.
     *
     * Gets a list of all users that are members in a given study group.
     *
     * @param groupID The group's unique identifier.
     * @return A list of all group member together with http status "200: OK".
     */
    @GetMapping("/{gid}/users")
    fun getUsersInGroup(@PathVariable(value = "gid") groupID: Long): ResponseEntity<List<StudyGroupMemberDAO>> {
        val users: List<StudyGroupMemberDAO>? = groupController.getUsersInGroup(groupID)
        if (users != null) {
            return ResponseEntity.ok(users)
        }
        else {
            return ResponseEntity.notFound().build()
        }
    }

    /**
     * This method is executed when PUT /groups/{id} is called.
     *
     * Update group with the Information provided.
     * Used when a group updates its information in the application, for example the description or the chapter of the lecture.
     * If there is no group with the groupID then the http status "404: NOT FOUND" is returned.
     * If the operation was successful then http status "200: OK" is returned.
     *
     * @param groupID The group's unique identifier.
     * @param updatedGroup The group's updated information.
     * @return The updated group together with a http status.
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
     * This method is executed when PUT /groups/{id}/join/{id} is called.
     *
     * A user can request membership in a study group he found and selected over the group search feature in the application.
     *
     * @param groupID The group's unique identifier.
     * @param userID The user's unique identifier who wants to join the group.
     * @return http status "200: OK" if the application to the group was successfully stored and http status "404: NOT FOUND" otherwise.
     */
    @PutMapping("/{gid}/join/{uid}")
    fun joinGroupRequest(@PathVariable(value = "gid") groupID: Long, @PathVariable(value = "uid") userID: Long): ResponseEntity<Void> {
        if (groupController.joinGroupRequest(groupID, userID)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when GET /groups/{id}/requests is called.
     *
     * Gets a list of all users that applied to the group and have not been accepted or declined yet.
     *
     * @param groupID The group's unique identifier.
     * @return http status "200: OK" if the group was found and the list could be generated together with the list of applying users and http status "404: NOT FOUND" otherwise.
     */
    @GetMapping("/{gid}/requests")
    fun openGroupRequests(@PathVariable(value = "gid") groupID: Long): ResponseEntity<List<UserDAO>> {
        val groupRequests : List<UserDAO> = groupController.getGroupRequests(groupID)
        return ResponseEntity.ok(groupRequests)
    }

    /**
     * This method is executed when PUT /groups/{id}/users/{id}/membership is called.
     *
     * Used to decide over an application of a user that applied to the group.
     * If positive, the applicating user will be a group member, if negative the application will be deleted.
     *
     * @param groupID The group's unique identifier.
     * @param userID The applicating user's unique identifier.
     * @param decision A boolean that is true if the applicating user will be accepted to the group and false otherwise.
     * @return http status "200: OK" if the application was found and the decision about it stored and http status "404: NOT FOUND" otherwise.
     */
    @PutMapping("/{gid}/users/{uid}/membership")
    fun toggleGroupRequest(@PathVariable(value = "gid") groupID: Long, @PathVariable(value = "uid") userID: Long,
                           @Valid @RequestBody decision: Boolean): ResponseEntity<StudyGroupMemberDAO?> {
        val groupMember: StudyGroupMemberDAO? = groupController.toggleGroupMembership(groupID, userID, decision)
        if (groupMember!=null) {
            if (groupMember.userID==0L && groupMember.groupID==0L) {
                return ResponseEntity.ok(null) //deleted
            } else {
                return ResponseEntity.ok(groupMember) //accepted
            }
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when DELETE /groups/{id}/users/{id} is called.
     *
     * A user can be removed from the group by and administrator of the group and
     * a user can quit a group on his own.
     * In both cases this method is used.
     *
     * @param groupID The group's unique identifier.
     * @param userID The unique id of the user that will be removed from the group.
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
     * This method is executed when DELETE /groups/{id} is called.
     *
     * As study groups do not last forever because they are often superfluous after the exam, a group can be deleted.
     *
     * @param groupID The group's unique identifier.
     * @return http status "200: OK" if the group was found and deleted and http status "404: NOT FOUND" otherwise.
     */
    @DeleteMapping("/{gid}")
    fun deleteGroup(@PathVariable(value = "gid") groupID: Long): ResponseEntity<Void> {
        if (groupController.deleteGroup(groupID)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when POST /groups/{id}/users/{id}/makeadmin is called.
     *
     * @param groupID The group's unique identifier.
     * @param userID The unique identifier of a user that shall be promoted to administrator in the given group.
     * @return http status "200: OK" if the group member found and successfully promoted and http status "404: NOT FOUND" otherwise.
     */
    @PostMapping("/{gid}/users/{uid}/makeadmin")
    fun makeUserAdmin(@PathVariable(value = "gid") groupID: Long, @PathVariable(value = "uid") userID: Long): ResponseEntity<Void> {
        if (groupController.makeUserAdminInGroup(groupID, userID)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when PUT /groups/{id}/report/{id} is called.
     *
     * As a group's details contain freetext, the freetext fields might contain inappropriate text.
     * Therefore, all users are able to report such inappropriate text to the moderation of the application.
     * This method is used to report a text field of a study group.
     *
     * @param groupID The unique identifier of the group that contains inappropriate information in its details
     * @param reporterID The reporting user's unique identifier.
     * @param field The descriptor of the exact field that contains inappropriate text.
     * @return http status "200: OK" if the report was successfully stored and http status "404: NOT FOUND" otherwise.
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
     * This method is executed when GET /groups/suggestion/{id} is called.
     *
     * Get group suggestions that may be relevant to a user.
     * These may be groups that belong to the same Major as the user.
     * Used to show hopefully relevant group's in the search view of the application,
     * before the user put in some text to search for that query.
     *
     * @param userID The unique identifier of the user.
     * @return Groups that may be relevant
     */
    @GetMapping("/suggestion/{uid}")
    fun getGroupSuggestions(@PathVariable(value = "uid") userID: Long): ResponseEntity<List<StudyGroupDAO>> {
        val groups : List<StudyGroupDAO>? = groupController.getGroupSuggestions(userID)
        if (groups!=null) {
            return ResponseEntity.ok(groups)
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * This method is executed when GET /groups/{id}/sessions is called.
     *
     * Gets a list of all study sessions a group has planned. The list might be empty.
     *
     * @param groupID The group's unique identifier.
     * @return A list of the planned study sessions together with http status "200: OK"
     */
    @GetMapping("/{gid}/sessions")
    fun getAllGroupSessions(@PathVariable(value = "gid") groupID: Long): ResponseEntity<List<SessionDAO>> {
        val sessions : List<SessionDAO> = sessionController.getAllGroupSessions(groupID)
        return ResponseEntity.ok(sessions)
    }

    /**
     * This method is executed when POST /groups/{id}/sessions is called.
     *
     * The main purpose of study groups is holding study sessions together.
     * With this method a new study session can be planned. The group members can signal their participation
     * in the session afterwards.
     *
     * @param session All information required to create the new study session.
     * @return The newly created study session including the id it got.
     */
    @PostMapping("/{gid}/sessions")
    fun createNewSession(@Valid @RequestBody session: SessionDAO): ResponseEntity<SessionDAO> =
        ResponseEntity.ok(sessionController.createSession(session))

    /**
     * This method is executed when GET /groups/{id}/hide is called.
     *
     * If the administrators of a study group regard the group as full or complete, they may want to prevent the
     * group from showing up in search results. That way, other users can no longer apply to it.
     * This method is used to hide the group from showing up in search results or to show up in search results again.
     *
     * @param groupID The group's unique identifier.
     * @param hidden A boolean which is true if the group shall not show up in search results and false otherwise.
     * @return http status "200: OK" if the group was found and it's hidden-status set successfully and http status "404: NOT FOUND" otherwise.
     */
    @PostMapping("/{gid}/hide")
    fun hideGroup(@PathVariable(value = "gid") groupID: Long, @Valid @RequestBody hidden: Boolean): ResponseEntity<Void> {
        if (groupController.hideGroup(groupID, hidden)) {
            return ResponseEntity<Void>(HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }
}