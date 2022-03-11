package ovh.studywithme.server

import com.beust.klaxon.Klaxon
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import ovh.studywithme.server.dao.*
import ovh.studywithme.server.model.SessionFrequency
import ovh.studywithme.server.model.SessionMode
import ovh.studywithme.server.model.StudyGroupField
import ovh.studywithme.server.model.StudyGroupMember

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integration")
@TestMethodOrder(MethodOrderer.Random::class)
class GroupTests : RestTests(){
        val trt = TestRestTemplate()

        @LocalServerPort
        var port: Int = 0

        @Test
        fun `Create a group and get it afterwards`() {
                val inst = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FHKA"), trt, port)
                val major = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Info"), trt, port)
                val lecture = post<LectureDAO,LectureDAO>("/majors/${major.majorID}/lectures", LectureDAO(0,"PSE",major.majorID), trt, port)
                val user = UserDetailDAO(0, "Hans", inst.institutionID, inst.name, major.majorID, major.name, "email@test.de","FHEASTH",false)
                //val createdUser = post<UserDetailDAO, UserDetailDAO>("/users", user, trt, port)

                val group = StudyGroupDAO(0,"Beste Lerngruppe?","Die coolsten!!",lecture.lectureID,SessionFrequency.ONCE,SessionMode.PRESENCE,3,100000,15)
                val createdGroup = post<StudyGroupDAO,StudyGroupDAO>("/groups/${user.userID}",group,trt,port)

                Assertions.assertNotEquals(0, createdGroup.groupID)
                Assertions.assertEquals(group.name, createdGroup.name)
                Assertions.assertEquals(group.description, createdGroup.description)
                Assertions.assertEquals(lecture.lectureID, createdGroup.lectureID)
                Assertions.assertEquals(group.sessionFrequency, createdGroup.sessionFrequency)
                Assertions.assertEquals(group.sessionType, createdGroup.sessionType)
                Assertions.assertEquals(group.lectureChapter, createdGroup.lectureChapter)
                Assertions.assertEquals(group.exercise, createdGroup.exercise)
                Assertions.assertEquals(1, createdGroup.memberCount)
        }

        fun createAGroup() : StudyGroupDAO {
                val inst = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FHKA"), trt, port)
                val major = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Info"), trt, port)
                val lecture = post<LectureDAO,LectureDAO>("/majors/${major.majorID}/lectures", LectureDAO(0,"PSE",major.majorID), trt, port)
                val user = UserDetailDAO(0, "Hans", inst.institutionID, inst.name, major.majorID, major.name, "email@test.de","FHEASTH",false)

                val group = StudyGroupDAO(0,"Beste Lerngruppe?","Die coolsten!!",lecture.lectureID,SessionFrequency.ONCE,SessionMode.PRESENCE,3,100000,15)
                return post<StudyGroupDAO,StudyGroupDAO>("/groups/${user.userID}",group,trt,port)
        }

        fun createAUser() : UserDetailDAO {
                val inst = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FHKA"), trt, port)
                val major = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Info"), trt, port)
                val user =  UserDetailDAO(0, "Hans", inst.institutionID, inst.name, major.majorID, major.name, "email@test.de","FHEASTH",false)
                return post<UserDetailDAO, UserDetailDAO>("/users", user, trt, port)
        }

        fun createASession(group:StudyGroupDAO) : SessionDAO {
                val newSession = SessionDAO(0,group.groupID,"Infobau",546161,1054645)
                return post("/groups/${group.groupID}/sessions",newSession, trt, port)
        }

        @Test
        fun `Get a group`() {
                val group = createAGroup()
                val fetchedGroup = get<StudyGroupDAO>("/groups/${group.groupID}",trt,port)
                Assertions.assertEquals(group,fetchedGroup)
        }

        @Test
        fun `Get a nonexistent Group`() {
                //val group = createAGroup()

                val fetchedGroup = getEx("/groups/0",trt,port)
                Assertions.assertEquals(HttpStatus.NOT_FOUND,fetchedGroup.statusCode)
        }

        @Test
        fun `User joins a Group`() {
                val group = createAGroup()
                val user = createAUser()
                val userList = get<List<StudyGroupMemberDAO>>("/groups/${group.groupID}/users", trt, port)

                val requestListBeforeJoin = getEx("/groups/${group.groupID}/requests", trt, port) //list requests before join
                Assertions.assertEquals("[]", requestListBeforeJoin.body)

                put<String,Void>("/groups/${group.groupID}/join/${user.userID}", "",trt,port) //request

                val requestList = getEx("/groups/${group.groupID}/requests", trt, port) //list requests while join
                Assertions.assertNotEquals("[]", requestList.body)

                put<Boolean,Void>("/groups/${group.groupID}/users/${user.userID}/membership", true,trt,port) //accept
                val userListAfterJoin = get<List<StudyGroupMemberDAO>>("/groups/${group.groupID}/users", trt, port)
                Assertions.assertNotEquals(userList, userListAfterJoin)

                val requestListAfterJoin = getEx("/groups/${group.groupID}/requests", trt, port) //list requests after join
                Assertions.assertEquals("[]", requestListAfterJoin.body)
        }

        @Test
        fun `User that does not exist joins a Group`() {
                val group = createAGroup()
                val userList = get<List<StudyGroupMemberDAO>>("/groups/${group.groupID}/users", trt, port)

                val requestListBeforeJoin = getEx("/groups/${group.groupID}/requests", trt, port) //list requests before join
                Assertions.assertEquals("[]", requestListBeforeJoin.body)

                var response = putEx("/groups/${group.groupID}/join/10645065", "",trt,port) //request
                Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

                val requestList = getEx("/groups/${group.groupID}/requests", trt, port) //list requests while join
                Assertions.assertEquals("[]", requestList.body)

                response = putEx("/groups/${group.groupID}/users/10645066/membership", true,trt,port) //accept
                Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

                val userListAfterJoin = get<List<StudyGroupMemberDAO>>("/groups/${group.groupID}/users", trt, port)
                Assertions.assertEquals(userList, userListAfterJoin)

                val requestListAfterJoin = getEx("/groups/${group.groupID}/requests", trt, port) //list requests after failed join
                Assertions.assertEquals("[]", requestListAfterJoin.body)
        }

        @Test
        fun `Toggle hidden status of a Group`() {
                val group = createAGroup()

                var hidden = get<Boolean>("/groups/${group.groupID}/hide",trt,port)
                Assertions.assertFalse(hidden)

                postEx("/groups/${group.groupID}/hide","",trt,port) //hide group
                hidden = get<Boolean>("/groups/${group.groupID}/hide",trt,port)
                Assertions.assertTrue(hidden)
                //TODO getGroups does not contain group

                postEx("/groups/${group.groupID}/hide","",trt,port) //unhide group
                hidden = get<Boolean>("/groups/${group.groupID}/hide",trt,port)
                Assertions.assertFalse(hidden)
        }

        @Test
        fun `Toggle hidden status of nonexistent Group`() {
                val hidden = getEx("/groups/0/hide",trt,port)
                Assertions.assertEquals(HttpStatus.NOT_FOUND, hidden.statusCode)
                Assertions.assertNull(hidden.body)

                val hide = postEx("/groups/0/hide","",trt,port) //hide group
                Assertions.assertEquals(HttpStatus.NOT_FOUND, hide.statusCode)
        }

        @Test
        fun `Test find all groups`() {
                val newGroup1 = createAGroup()
                val newGroup2 = createAGroup()
                val fetchedGroups = getEx("/groups", trt, port)
                assertNotNull(fetchedGroups.body)
                val groupList: List<StudyGroupDAO>? = fetchedGroups.body?.let { Klaxon().parseArray(it) }

                Assertions.assertEquals(true, groupList!!.contains(newGroup1))
                Assertions.assertEquals(true, groupList!!.contains(newGroup2))
        }

        @Test
        fun `Create groups and check if they appear in getall groups by name`(){
                val newGroup1 = createAGroup()
                var fetchedGroups = getEx("/groups/?text="+java.net.URLEncoder.encode(newGroup1.name, "utf-8"), trt, port)
                assertNotNull(fetchedGroups.body)
                assertNotEquals("[]",fetchedGroups.body)
                var groupList: List<StudyGroupDAO>? = fetchedGroups.body?.let { Klaxon().parseArray(it) }
                Assertions.assertEquals(true, groupList!!.contains(newGroup1))

                fetchedGroups = getEx("/groups/?name="+java.net.URLEncoder.encode(newGroup1.name, "utf-8"), trt, port)
                assertNotNull(fetchedGroups.body)
                assertNotEquals("[]",fetchedGroups.body)
                groupList = fetchedGroups.body?.let { Klaxon().parseArray(it) }
                Assertions.assertEquals(true, groupList!!.contains(newGroup1))

                val lecture = get<LectureDAO>("/lectures/${newGroup1.lectureID}", trt, port)
                fetchedGroups = getEx("/groups/?lecture="+java.net.URLEncoder.encode(lecture.name.toString(), "utf-8"), trt, port)
                assertNotNull(fetchedGroups.body)
                assertNotEquals("[]",fetchedGroups.body)
                groupList = fetchedGroups.body?.let { Klaxon().parseArray(it) }
                Assertions.assertEquals(true, groupList!!.contains(newGroup1))
        }

        @Test
        fun `Create group and check if it appears in the suggestions`(){
                val newGroup1 = createAGroup()
                val lecture = get<LectureDAO>("/lectures/${newGroup1.lectureID}", trt, port)
                val major = get<MajorDAO>("/majors/${lecture.majorID}", trt, port)
                val institution = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0,"Insti"), trt, port)
                val user = UserDetailDAO(0,"Hans", institution.institutionID, institution.name, major.majorID, major.name, "cont", "firebase", false)
                val userResponse = post<UserDetailDAO, UserDetailDAO>("/users", user, trt, port)
                var fetchedGroups = getEx("/groups/suggestion/"+userResponse.userID, trt, port)
                assertNotNull(fetchedGroups.body)
                assertNotEquals("[]",fetchedGroups.body)
                var groupList: List<StudyGroupDAO>? = fetchedGroups.body?.let { Klaxon().parseArray(it) }
                assertEquals(true, groupList!!.contains(newGroup1))
                assertEquals(groupList.map { it.lectureID == lecture.lectureID }.contains(false),false)
        }

        @Test
        fun `Get suggestions for nonexistent user`(){
                var fetchedGroups = getEx("/groups/suggestion/10654605", trt, port)
                assertNull(fetchedGroups.body)
                assertEquals(HttpStatus.NOT_FOUND, fetchedGroups.statusCode)
        }

        @Test
        fun `Update Group`() {
                val newGroup = createAGroup()
                val major = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "New Major"), trt, port)
                val lecture = post<LectureDAO,LectureDAO>("/majors/${major.majorID}/lectures", LectureDAO(0,"New Lecture",major.majorID), trt, port)
                val updatedGroup = StudyGroupDAO(newGroup.groupID,"changed Name", "changed Description", lecture.lectureID, SessionFrequency.MONTHLY, SessionMode.HYBRID, 1001, 1002, 855464)
                val gotGroup = put<StudyGroupDAO,StudyGroupDAO>("/groups/${newGroup.groupID}", updatedGroup, trt, port)
                assertNotNull(gotGroup)

                assertEquals(updatedGroup.groupID,              gotGroup!!.groupID)
                assertEquals(updatedGroup.name,                 gotGroup.name)
                assertEquals(updatedGroup.description,          gotGroup.description)
                assertEquals(updatedGroup.lectureID,            gotGroup.lectureID)
                assertEquals(updatedGroup.sessionFrequency,     gotGroup.sessionFrequency)
                assertEquals(updatedGroup.sessionType,          gotGroup.sessionType)
                assertEquals(updatedGroup.lectureChapter,       gotGroup.lectureChapter)
                assertEquals(updatedGroup.exercise,             gotGroup.exercise)
                assertNotEquals(gotGroup.memberCount,              updatedGroup.memberCount)

                assertEquals(newGroup.groupID,                  gotGroup!!.groupID)
                assertNotEquals(newGroup.name,                  gotGroup.name)
                assertNotEquals(newGroup.description,           gotGroup.description)
                assertNotEquals(newGroup.lectureID,             gotGroup.lectureID)
                assertNotEquals(newGroup.sessionFrequency,      gotGroup.sessionFrequency)
                assertNotEquals(newGroup.sessionType,           gotGroup.sessionType)
                assertNotEquals(newGroup.lectureChapter,        gotGroup.lectureChapter)
                assertNotEquals(newGroup.exercise,              gotGroup.exercise)
                assertEquals(newGroup.memberCount,              gotGroup.memberCount)
        }

        @Test
        fun `Update a nonexistent Group`() {
                val newGroup = createAGroup()
                val major = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "New Majsisnrtaor"), trt, port)
                val lecture = post<LectureDAO,LectureDAO>("/majors/${major.majorID}/lectures", LectureDAO(0,"New Lecturieasnrte",major.majorID), trt, port)
                var updatedGroup = StudyGroupDAO(400546,"changed Name", "changed Description", lecture.lectureID, SessionFrequency.MONTHLY, SessionMode.HYBRID, 1001, 1002, 855464)

                var gotGroup = putEx("/groups/${newGroup.groupID}", updatedGroup, trt, port) //wrong groupid in group
                assertEquals(HttpStatus.NOT_FOUND,gotGroup.statusCode)

                updatedGroup = StudyGroupDAO(newGroup.groupID,"changed Name", "changed Description", lecture.lectureID, SessionFrequency.MONTHLY, SessionMode.HYBRID, 1001, 1002, 855464)
                gotGroup = putEx("/groups/400546", updatedGroup, trt, port) //wrong groupid in url
                assertEquals(HttpStatus.NOT_FOUND,gotGroup.statusCode)

                updatedGroup = StudyGroupDAO(newGroup.groupID,"changed Name", "changed Description", 105486465, SessionFrequency.MONTHLY, SessionMode.HYBRID, 1001, 1002, 855464)
                gotGroup = putEx("/groups/${newGroup.groupID}", updatedGroup, trt, port) //wrong lectureid in group
                assertEquals(HttpStatus.NOT_FOUND,gotGroup.statusCode)
        }

        @Test
        fun `Report a group`() {
                val group = createAGroup()
                val user = createAUser()
                val response = putEx("/groups/${group.groupID}/report/${user.userID}", StudyGroupField.DESCRIPTION, trt, port)
                assertEquals(HttpStatus.OK ,response.statusCode)
        }

        @Test
        fun `Report a group that does not exist`() {
                val user = createAUser()
                val response = putEx("/groups/1064040/report/${user.userID}", StudyGroupField.DESCRIPTION, trt, port)
                assertEquals(HttpStatus.NOT_FOUND ,response.statusCode)
        }

        @Test
        fun `Report a group with a user that does not exist`() {
                val group = createAGroup()
                val response = putEx("/groups/${group.groupID}/report/1065460", StudyGroupField.DESCRIPTION, trt, port)
                assertEquals(HttpStatus.NOT_FOUND ,response.statusCode)
        }

        @Test
        fun `Get user in a group that does not exist`() {
                val result = getEx("/groups/105464506/users", trt, port)
                assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        }

        @Test
        fun `Remove last user in group`(){
                val inst = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FHKA"), trt, port)
                val major = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Info"), trt, port)
                val lecture = post<LectureDAO,LectureDAO>("/majors/${major.majorID}/lectures", LectureDAO(0,"PSE",major.majorID), trt, port)
                val user = UserDetailDAO(0, "Hans", inst.institutionID, inst.name, major.majorID, major.name, "email@test.de","FHEASTH",false)
                var group = StudyGroupDAO(0,"Beste Lerngruppe?","Die coolsten!!",lecture.lectureID,SessionFrequency.ONCE,SessionMode.PRESENCE,3,100000,15)
                group = post<StudyGroupDAO,StudyGroupDAO>("/groups/${user.userID}",group,trt,port)
                val response = deleteEx("/groups/${group.groupID}/users/${user.userID}", trt, port)
                assertEquals(HttpStatus.OK, response.statusCode)

                val getGroup = getEx("/groups/${group.groupID}", trt, port)
                //assertEquals(HttpStatus.NOT_FOUND, getGroup.statusCode) //TODO
                
                val getLecture = getEx("/lectures/${lecture.lectureID}", trt, port)
                //assertEquals(HttpStatus.NOT_FOUND, getLecture.statusCode) //TODO
        }

        @Test
        fun `Remove last user in group but lecture is used elsewhere`(){
                val inst = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FHKA"), trt, port)
                val major = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Info"), trt, port)
                val lecture = post<LectureDAO,LectureDAO>("/majors/${major.majorID}/lectures", LectureDAO(0,"PSE",major.majorID), trt, port)
                val user = UserDetailDAO(0, "Hans", inst.institutionID, inst.name, major.majorID, major.name, "email@test.de","FHEASTH",false)
                var group = StudyGroupDAO(0,"Beste Lerngruppe?","Die coolsten!!",lecture.lectureID,SessionFrequency.ONCE,SessionMode.PRESENCE,3,100000,15)
                val group2 = post<StudyGroupDAO,StudyGroupDAO>("/groups/${user.userID}",group,trt,port)
                group = post<StudyGroupDAO,StudyGroupDAO>("/groups/${user.userID}",group,trt,port) //create group1
                post<StudyGroupDAO,StudyGroupDAO>("/groups/${user.userID}",group2,trt,port)     //create group2

                val response = deleteEx("/groups/${group.groupID}/users/${user.userID}", trt, port)  //delete last user in group1
                assertEquals(HttpStatus.OK, response.statusCode)

                val getGroup = getEx("/groups/${group.groupID}", trt, port) //get group1
                //assertEquals(HttpStatus.NOT_FOUND, getGroup.statusCode) //TODO

                val getLecture = getEx("/lectures/${lecture.lectureID}", trt, port) //get lecture of group1
                assertEquals(HttpStatus.OK, getLecture.statusCode)
                assertNotNull(getLecture.body)
                assertNotEquals("[]",getLecture.body)
                var lectureResponse: LectureDAO? = getLecture.body?.let { Klaxon().parse(it) }
                assertEquals(lecture, lectureResponse)
        }

        @Test
        fun `Make User Admin in Group`(){
                val group = createAGroup()
                val user = createAUser()

                put<String,Void>("/groups/${group.groupID}/join/${user.userID}", "",trt,port) //request
                put<Boolean,Void>("/groups/${group.groupID}/users/${user.userID}/membership", true,trt,port) //accept
                val userListBeforeAdmin = getEx("/groups/${group.groupID}/users", trt, port)
                assertNotNull(userListBeforeAdmin.body)
                assertNotEquals("[]",userListBeforeAdmin.body)
                var userParsed: List<StudyGroupMemberDAO>? = userListBeforeAdmin.body?.let { Klaxon().parseArray(it) }
                assertEquals(true, userParsed!!.contains<StudyGroupMemberDAO>(StudyGroupMemberDAO(user.userID,group.groupID,user.name,false)))
                assertEquals(false, userParsed!!.contains<StudyGroupMemberDAO>(StudyGroupMemberDAO(user.userID,group.groupID,user.name,true)))

                postEx("/groups/${group.groupID}/users/${user.userID}/makeadmin", "", trt, port) //make admin
                val userListAfterAdmin = getEx("/groups/${group.groupID}/users", trt, port)
                assertNotNull(userListAfterAdmin.body)
                assertNotEquals("[]",userListAfterAdmin.body)
                userParsed = userListAfterAdmin.body?.let { Klaxon().parseArray(it) }
                assertEquals(true, userParsed!!.contains<StudyGroupMemberDAO>(StudyGroupMemberDAO(user.userID,group.groupID,user.name,true)))
                assertEquals(false, userParsed!!.contains<StudyGroupMemberDAO>(StudyGroupMemberDAO(user.userID,group.groupID,user.name,false)))
        }

        @Test
        fun `Make User Admin that is already admin in Group`(){
                val group = createAGroup()
                val user = createAUser()

                put<String,Void>("/groups/${group.groupID}/join/${user.userID}", "",trt,port) //request
                put<Boolean,Void>("/groups/${group.groupID}/users/${user.userID}/membership", true,trt,port) //accept
                postEx("/groups/${group.groupID}/users/${user.userID}/makeadmin", "", trt, port) //make admin
                val secondAdmin = postEx("/groups/${group.groupID}/users/${user.userID}/makeadmin", "", trt, port) //make admin
                assertEquals(HttpStatus.NOT_FOUND, secondAdmin.statusCode)
                val userListAfterAdmin = getEx("/groups/${group.groupID}/users", trt, port)
                assertNotNull(userListAfterAdmin.body)
                assertNotEquals("[]",userListAfterAdmin.body)
                val userParsed: List<StudyGroupMemberDAO>? = userListAfterAdmin.body?.let { Klaxon().parseArray(it) }
                assertEquals(true, userParsed!!.contains<StudyGroupMemberDAO>(StudyGroupMemberDAO(user.userID,group.groupID,user.name,true)))
                assertEquals(false, userParsed!!.contains<StudyGroupMemberDAO>(StudyGroupMemberDAO(user.userID,group.groupID,user.name,false)))
        }

        @Test
        fun `Make User Admin that does not exist`(){
                val group = createAGroup()
                val user = createAUser()

                //nonexistent user
                var response = putEx("/groups/${group.groupID}/join/20405465", "",trt,port) //fake request
                assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
                response = putEx("/groups/${group.groupID}/users/20405465/membership", true,trt,port) //fake accept
                assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
                response = postEx("/groups/${group.groupID}/users/20405465/makeadmin", "", trt, port) //make admin
                assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

                //nonexistent group
                response = putEx("/groups/20405465/join/${user.userID}", "",trt,port) //fake request
                assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
                response = putEx("/groups/20405465/users/${user.userID}/membership", true,trt,port) //fake accept
                assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
                response = postEx("/groups/20405465/users/${user.userID}/makeadmin", "", trt, port) //make admin
                assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        }

        @Test
        fun `Delete a user while joining`(){
                /*
                response = putEx("/groups/${group.groupID}/join/${user.userID}", "",trt,port) //real request
                put<Boolean,Void>("/groups/${group.groupID}/users/${user.userID}/membership", true,trt,port) //accept
                postEx("/groups/${group.groupID}/users/${user.userID}/makeadmin", "", trt, port) //make admin
                val secondAdmin = postEx("/groups/${group.groupID}/users/${user.userID}/makeadmin", "", trt, port) //make admin
                assertEquals(HttpStatus.NOT_FOUND, secondAdmin.statusCode)
                val userListAfterAdmin = getEx("/groups/${group.groupID}/users", trt, port)
                assertNotNull(userListAfterAdmin.body)
                assertNotEquals("[]",userListAfterAdmin.body)
                val userParsed: List<StudyGroupMemberDAO>? = userListAfterAdmin.body?.let { Klaxon().parseArray(it) }
                assertEquals(true, userParsed!!.contains<StudyGroupMemberDAO>(StudyGroupMemberDAO(user.userID,group.groupID,user.name,true)))
                assertEquals(false, userParsed!!.contains<StudyGroupMemberDAO>(StudyGroupMemberDAO(user.userID,group.groupID,user.name,false)))
                 */ //TODO
        }

        @Test
        fun `Delete a Group`(){
                val inst = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FHKA"), trt, port)
                val major = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Info"), trt, port)
                val lecture = post<LectureDAO,LectureDAO>("/majors/${major.majorID}/lectures", LectureDAO(0,"PSE",major.majorID), trt, port)
                val user = UserDetailDAO(0, "Hans", inst.institutionID, inst.name, major.majorID, major.name, "email@test.de","FHEASTH",false)
                var group = StudyGroupDAO(0,"Beste Lerngruppe?","Die coolsten!!",lecture.lectureID,SessionFrequency.ONCE,SessionMode.PRESENCE,3,100000,15)
                group = post("/groups/${user.userID}",group,trt,port) //create group

                val response = deleteEx("/groups/${group.groupID}", trt,port) //delete group
                assertEquals(HttpStatus.OK, response.statusCode)

                val getGroup = getEx("/groups/${group.groupID}", trt, port) //get group1
                assertEquals(HttpStatus.NOT_FOUND, getGroup.statusCode)

                val getLecture = getEx("/lectures/${lecture.lectureID}", trt, port) //get lecture of group1
                //assertEquals(HttpStatus.NOT_FOUND, getLecture.statusCode) //TODO
                //assertNull(getLecture.body) //TODO
                assertNotEquals("[]",getLecture.body)
        }

        @Test
        fun `Delete a Group that does not exist`(){
                val response = deleteEx("/groups/4604560", trt,port) //fake delete group
                assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
                assertNull(response.body)
        }

        @Test
        fun `Remove a user from a Group that is not in that group`(){
                val user = createAUser()
                val group = createAGroup()
                var response = deleteEx("/groups/${group.groupID}/users/2040654", trt,port) //fake user remove
                assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

                response = deleteEx("/groups/6065406/users/${user.userID}", trt,port) //fake user remove
                assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        }

}