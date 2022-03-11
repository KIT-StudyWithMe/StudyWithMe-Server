package ovh.studywithme.server

import com.beust.klaxon.Klaxon
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.TestInstance
import org.springframework.http.HttpStatus
import ovh.studywithme.server.dao.*
import ovh.studywithme.server.model.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Tag("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Random::class)
class ReportTests : RestTests() {

    val trt = TestRestTemplate()

    @LocalServerPort
    var port: Int = 0

    private fun createAUser() : UserDetailDAO {
        val inst = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FHKA"), trt, port)
        val major = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Info"), trt, port)
        val user = UserDetailDAO(
            0,
            "Hans",
            inst.institutionID,
            inst.name,
            major.majorID,
            major.name,
            "email@test.de",
            "f1r3b453u1d",
            false
        )
        return post<UserDetailDAO, UserDetailDAO>("/users", user, trt, port)
    }

    private fun createAGroup() : StudyGroupDAO {
        val inst = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FHKA"), trt, port)
        val major = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Informatik"), trt, port)
        val lecture = post<LectureDAO,LectureDAO>("/majors/${major.majorID}/lectures", LectureDAO(0,"PSE",major.majorID), trt, port)
        val user = UserDetailDAO(0, "Hans", inst.institutionID, inst.name, major.majorID, major.name, "email@test.de","FHEASTH",false)
        //val createdUser = post<UserDetailDAO, UserDetailDAO>("/users", user, trt, port)

        val group = StudyGroupDAO(0,"Beste Lerngruppe?","Die coolsten!!",lecture.lectureID,
            SessionFrequency.ONCE,
            SessionMode.PRESENCE,4,9,7)
        return post<StudyGroupDAO, StudyGroupDAO>("/groups/${user.userID}",group,trt,port)
    }

    fun createASession(group:StudyGroupDAO) : SessionDAO {
        val newSession = SessionDAO(0,group.groupID,"Infobau",546161,1054645)
        return post("/groups/${group.groupID}/sessions",newSession, trt, port)
    }

    @Test
    fun `Create group reports and verify the list of all group reports contains them`() {
        // creating new users ensures their ID is brand new and not used by someone else
        val user1 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)
        val user2 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)
        val user3 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)

        val group1 = post<StudyGroupDAO, StudyGroupDAO>("/groups/${user1.userID}", createAGroup(), trt, port)
        val group2 = post<StudyGroupDAO, StudyGroupDAO>("/groups/${user3.userID}", createAGroup(), trt, port)
        val group3 = post<StudyGroupDAO, StudyGroupDAO>("/groups/${user2.userID}", createAGroup(), trt, port)

        // those don't return anything, hence the workaround with the reporterID later on when using contains
        put<StudyGroupField, Void>("/groups/${group1.groupID}/report/${user2.userID}", StudyGroupField.DESCRIPTION, trt, port)
        put<StudyGroupField, Void>("/groups/${group2.groupID}/report/${user1.userID}", StudyGroupField.NAME, trt, port)
        put<StudyGroupField, Void>("/groups/${group1.groupID}/report/${user3.userID}", StudyGroupField.LECTURE, trt, port)
        put<StudyGroupField, Void>("/groups/${group3.groupID}/report/${user3.userID}", StudyGroupField.DESCRIPTION, trt, port)

        val fetchedGroupReports = getEx("/reports/group", trt, port)
        val body : String? = fetchedGroupReports.body
        assertNotNull(body)
        val parsedList:List<StudyGroupReportDAO>? = body?.let { Klaxon().parseArray(it) }

        assertEquals(true, parsedList!!.map { it.reporterID }.contains(user1.userID))
        assertEquals(true, parsedList.map { it.reporterID }.contains(user2.userID))
        assertEquals(2, parsedList.map { it.reporterID }.filter { it == user3.userID }.size)
    }

    @Test
    fun `Create user reports and verify the list of all user reports contains them`() {
        // creating new users ensures their ID is brand new and not used by someone else
        val user1 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)
        val user2 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)
        val user3 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)

        // those don't return anything, hence the workaround with the reporterID later on when using contains
        put<UserField, Void>("/users/${user2.userID}/report/${user1.userID}", UserField.CONTACT, trt, port)
        put<UserField, Void>("/users/${user3.userID}/report/${user2.userID}", UserField.NAME, trt, port)
        put<UserField, Void>("/users/${user1.userID}/report/${user3.userID}", UserField.MAJOR, trt, port)
        put<UserField, Void>("/users/${user1.userID}/report/${user2.userID}", UserField.INSTITUTION, trt, port)

        val fetchedGroupReports = getEx("/reports/user", trt, port)
        val body : String? = fetchedGroupReports.body
        assertNotNull(body)
        val parsedList:List<UserReportDAO>? = body?.let { Klaxon().parseArray(it) }

        assertEquals(true, parsedList!!.map { it.reporterID }.contains(user1.userID))
        assertEquals(true, parsedList.map { it.reporterID }.contains(user2.userID))
        assertEquals(2, parsedList.map { it.reporterID }.filter { it == user2.userID }.size)
    }

    @Test
    fun `Create session reports and verify the list of all session reports contains them`() {
        // creating new users ensures their ID is brand new and not used by someone else
        val user1 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)
        val user2 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)
        val user3 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)

        val group1 = post<StudyGroupDAO, StudyGroupDAO>("/groups/${user1.userID}", createAGroup(), trt, port)
        val group2 = post<StudyGroupDAO, StudyGroupDAO>("/groups/${user2.userID}", createAGroup(), trt, port)
        val group3 = post<StudyGroupDAO, StudyGroupDAO>("/groups/${user3.userID}", createAGroup(), trt, port)

        val session1 = post<SessionDAO, SessionDAO>("/groups/${group1.groupID}/sessions/", createASession(group1), trt, port)
        val session2 = post<SessionDAO, SessionDAO>("/groups/${group2.groupID}/sessions/", createASession(group2), trt, port)
        val session3 = post<SessionDAO, SessionDAO>("/groups/${group2.groupID}/sessions/", createASession(group2), trt, port)
        val session4 = post<SessionDAO, SessionDAO>("/groups/${group3.groupID}/sessions/", createASession(group3), trt, port)

        // those don't return anything, hence the workaround with the reporterID later on when using contains
        put<SessionField, Void>("/sessions/${session1.sessionID}/report/${user2.userID}", SessionField.PLACE, trt, port)
        put<SessionField, Void>("/sessions/${session2.sessionID}/report/${user1.userID}", SessionField.PLACE, trt, port)
        put<SessionField, Void>("/sessions/${session3.sessionID}/report/${user3.userID}", SessionField.PLACE, trt, port)
        put<SessionField, Void>("/sessions/${session4.sessionID}/report/${user3.userID}", SessionField.PLACE, trt, port)

        val fetchedGroupReports = getEx("/reports/session", trt, port)
        val body : String? = fetchedGroupReports.body
        assertNotNull(body)
        val parsedList:List<SessionReportDAO>? = body?.let { Klaxon().parseArray(it) }

        assertEquals(true, parsedList!!.map { it.reporterID }.contains(user1.userID))
        assertEquals(true, parsedList.map { it.reporterID }.contains(user2.userID))
        assertEquals(2, parsedList.map { it.reporterID }.filter { it == user3.userID }.size)
    }

    @Test
    fun `Create and then delete a group report`() {
        val user1 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)
        val user2 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)
        val group = post<StudyGroupDAO, StudyGroupDAO>("/groups/${user2.userID}", createAGroup(), trt, port)

        put<StudyGroupField, Void>("/groups/${group.groupID}/report/${user1.userID}", StudyGroupField.DESCRIPTION, trt, port)
        put<StudyGroupField, Void>("/groups/${group.groupID}/report/${user2.userID}", StudyGroupField.DESCRIPTION, trt, port)
        delete("/reports/group/${user1.userID}/${group.groupID}/${StudyGroupField.DESCRIPTION}", trt, port)

        val fetchedGroupReports = getEx("/reports/group", trt, port)
        val body : String? = fetchedGroupReports.body
        assertNotNull(body)
        val parsedList:List<StudyGroupReportDAO>? = body?.let { Klaxon().parseArray(it) }

        assertEquals(false, parsedList!!.map { it.reporterID }.contains(user1.userID))
        assertEquals(true, parsedList.map { it.reporterID }.contains(user2.userID))
    }

    @Test
    fun `Create and then delete a user report`() {
        val user1 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)
        val user2 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)
        val user3 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)


        put<UserField, Void>("/users/${user3.userID}/report/${user1.userID}", UserField.CONTACT, trt, port)
        put<UserField, Void>("/users/${user3.userID}/report/${user2.userID}", UserField.NAME, trt, port)
        delete("/reports/user/${user1.userID}/${user3.userID}/${UserField.CONTACT}", trt, port)

        val fetchedGroupReports = getEx("/reports/user", trt, port)
        val body : String? = fetchedGroupReports.body
        assertNotNull(body)
        val parsedList:List<UserReportDAO>? = body?.let { Klaxon().parseArray(it) }

        assertEquals(false, parsedList!!.map { it.reporterID }.contains(user1.userID))
        assertEquals(true, parsedList.map { it.reporterID }.contains(user2.userID))
    }

    @Test
    fun `Create and then delete a session report`() {
        val user1 = createAUser(trt, port)
        val user2 = createAUser(trt, port)

        val group1 = createAGroup(trt, port)
        val group2 = createAGroup(trt, port)

        val session1 = createASession(group1,trt, port)
        val session2 = createASession(group2,trt, port)

        put<SessionField, Void>("/sessions/${session1.sessionID}/report/${user2.userID}", SessionField.PLACE, trt, port) //report a session
        put<SessionField, Void>("/sessions/${session2.sessionID}/report/${user1.userID}", SessionField.PLACE, trt, port) //report a session
        delete("/reports/session/${user2.userID}/${session1.sessionID}/${SessionField.PLACE}", trt, port)

        val fetchedGroupReports = getEx("/reports/session", trt, port)
        val body : String? = fetchedGroupReports.body
        assertNotNull(body)
        val parsedList:List<SessionReportDAO>? = body?.let { Klaxon().parseArray(it) }

        assertEquals(true, parsedList!!.map { it.reporterID }.contains(user1.userID))
        assertEquals(false, parsedList.map { it.reporterID }.contains(user2.userID))
    }

    @Test
    fun `Report a non-existent Session`(){
        val session = createASession(trt, port)
        val user = createAUser(trt, port)
        var response = putEx("/sessions/406546/report/${user.userID}", SessionField.PLACE, trt, port) //report fake session
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        response = putEx("/sessions/${session.sessionID}/report/106546", SessionField.PLACE, trt, port) //report session with fake user
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `Try deleting non-existent group report`() {
        val result = deleteEx("/reports/group/0/0/${StudyGroupField.DESCRIPTION}", trt, port)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    @Test
    fun `Try deleting non-existent user report`() {
        val result = deleteEx("/reports/user/0/0/${UserField.CONTACT}", trt, port)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    @Test
    fun `Try deleting non-existent session report`() {
        val result = deleteEx("/reports/session/0/0/${SessionField.PLACE}", trt, port)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }
}