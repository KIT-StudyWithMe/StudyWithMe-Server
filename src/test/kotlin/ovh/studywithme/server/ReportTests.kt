package ovh.studywithme.server

import com.beust.klaxon.Klaxon
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.TestInstance
import org.springframework.web.bind.annotation.PutMapping
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

    @Test
    fun `Create group reports and verify the list of all group reports contains them`() {
        // creating new users ensures their ID is brand new and not used by someone else
        val user1 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)
        val user2 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)
        val user3 = post<UserDetailDAO, UserDetailDAO>("/users", createAUser(), trt, port)

        val group1 = post<StudyGroupDAO,StudyGroupDAO>("/groups/${user1.userID}", createAGroup(), trt, port)
        val group2 = post<StudyGroupDAO,StudyGroupDAO>("/groups/${user3.userID}", createAGroup(), trt, port)
        val group3 = post<StudyGroupDAO,StudyGroupDAO>("/groups/${user2.userID}", createAGroup(), trt, port)

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
}