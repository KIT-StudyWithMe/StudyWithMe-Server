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
import ovh.studywithme.server.model.Session
import ovh.studywithme.server.model.SessionFrequency
import ovh.studywithme.server.model.SessionMode

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
                val createdUser = post<UserDetailDAO, UserDetailDAO>("/users", user, trt, port)

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
                val createdUser = post<UserDetailDAO, UserDetailDAO>("/users", user, trt, port)

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
                val group = createAGroup()

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
                //Assertions.assertNotEquals(userList, userListAfterJoin) //TODO

                val requestListAfterJoin = getEx("/groups/${group.groupID}/requests", trt, port) //list requests after join
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
                //assertNotEquals(updatedGroup.memberCount,       gotGroup.memberCount) //TODO

                assertEquals(newGroup.groupID,                  gotGroup!!.groupID)
                assertNotEquals(newGroup.name,                  gotGroup.name)
                assertNotEquals(newGroup.description,           gotGroup.description)
                assertNotEquals(newGroup.lectureID,             gotGroup.lectureID)
                assertNotEquals(newGroup.sessionFrequency,      gotGroup.sessionFrequency)
                assertNotEquals(newGroup.sessionType,           gotGroup.sessionType)
                assertNotEquals(newGroup.lectureChapter,        gotGroup.lectureChapter)
                assertNotEquals(newGroup.exercise,              gotGroup.exercise)
                assertNotEquals(newGroup.memberCount,           gotGroup.memberCount)

                assertNotEquals(newGroup.memberCount,           updatedGroup.memberCount)
        }

        @Test
        fun `Update a nonexistent Group`() {

        }

        @Test
        fun `Create a session and get it`() {

        }


}