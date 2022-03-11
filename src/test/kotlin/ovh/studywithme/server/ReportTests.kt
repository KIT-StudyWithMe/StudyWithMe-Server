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
import ovh.studywithme.server.model.SessionFrequency
import ovh.studywithme.server.model.SessionMode
import ovh.studywithme.server.model.StudyGroupField
import ovh.studywithme.server.model.StudyGroupReport


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

    private var firstMajor = MajorDAO(0, "Informatik")

    @BeforeAll
    fun init() {
        firstMajor = post("/majors", firstMajor, trt, port)
    }

    @AfterAll
    fun tearDown() {
        delete("/majors/${firstMajor.majorID}", trt, port)
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
        val parsedList:List<StudyGroupReport>? = body?.let { Klaxon().parseArray(it) }

        assertEquals(true, parsedList!!.map { it.reporterID }.contains(user1.userID))
        assertEquals(true, parsedList.map { it.reporterID }.contains(user2.userID))
        assertEquals(2, parsedList.map { it.reporterID }.filter { it == user3.userID }.size)
    }

    @Test
    fun `Create a new institution and request it`() {
        val institutionData = InstitutionDAO(0, "KIT")
        val newInstitution = post<InstitutionDAO, InstitutionDAO>("/institutions", institutionData, trt, port)
        val fetchedInstitution = get<InstitutionDAO>("/institutions/" + newInstitution.institutionID, trt, port)

        assertEquals(newInstitution, fetchedInstitution)
        assertEquals(institutionData.name, fetchedInstitution.name)
        assertNotEquals(institutionData.institutionID, fetchedInstitution.institutionID)
    }

    @Test
    fun `Delete a created institution`() {
        val institutionData = InstitutionDAO(0, "Fernuni Hagen")
        val newInstitution = post<InstitutionDAO, InstitutionDAO>("/institutions", institutionData, trt, port)

        delete("/institutions/${newInstitution.institutionID}", trt, port)
        val result = getEx("/institutions/${newInstitution.institutionID}", trt, port)
        val body = getBody(result)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertEquals("", body)
    }

    @Test
    fun `Delete a non-existing institution`() {
        val result = deleteEx("/institutions/0", trt, port)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    //###############
    //# major tests #
    //###############

    @Test
    fun `Get major with ID 0 and expect nothing`() {
        val result = getEx("/majors/0", trt, port)
        val body = getBody(result)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertEquals("", body)
    }

    @Test
    fun `Create some majors and verify the list of all majors contains them`() {
        val newMajor1 = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Chemie"), trt, port)
        val newMajor2 = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Gender Studies"), trt, port)

        val fetchedInstitutions = getEx("/majors", trt, port)
        val body : String? = fetchedInstitutions.body
        assertNotNull(body)
        val parsedList:List<MajorDAO>? = body?.let { Klaxon().parseArray(it) }

        assertEquals(true, parsedList!!.contains(newMajor1))
        assertEquals(true, parsedList.contains(newMajor2))
    }

    @Test
    fun `Create a new major and request it`() {
        val majorData = MajorDAO(0, "Informatik")
        val newMajor = post<MajorDAO, MajorDAO>("/majors", majorData, trt, port)
        val fetchedMajor = get<MajorDAO>("/majors/" + newMajor.majorID, trt, port)

        assertEquals(newMajor, fetchedMajor)
        assertEquals(majorData.name, fetchedMajor.name)
        assertNotEquals(majorData.majorID, fetchedMajor.majorID)
    }

    @Test
    fun `Delete a created major`() {
        val majorData = MajorDAO(0, "Mandala Informatik")
        val newMajor = post<MajorDAO, MajorDAO>("/majors", majorData, trt, port)

        delete("/majors/${newMajor.majorID}", trt, port)
        val result = getEx("/majors/${newMajor.majorID}", trt, port)
        val body = getBody(result)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertEquals("", body)
    }

    @Test
    fun `Delete a non-existing major`() {
        val result = deleteEx("/majors/0", trt, port)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    //#################
    //# lecture tests #
    //#################

    @Test
    fun `Get lecture with ID 0 and expect nothing`() {
        val result = getEx("/lectures/0", trt, port)
        val body = getBody(result)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertEquals("", body)
    }

    @Test
    fun `Create some lectures and verify the list of all majors contains them`() {
        val newMajor = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Physik"), trt, port)

        val newLecture1 = post<LectureDAO, MajorDAO>("/majors/${newMajor.majorID}/lectures", LectureDAO(0, "Ex Physik III", newMajor.majorID), trt, port)
        val newLecture2 = post<LectureDAO, MajorDAO>("/majors/${newMajor.majorID}/lectures", LectureDAO(0, "Theo Physik IV", newMajor.majorID), trt, port)

        val fetchedInstitutions = getEx("/majors/${newMajor.majorID}/lectures", trt, port)
        val body : String? = fetchedInstitutions.body
        assertNotNull(body)
        val parsedList:List<MajorDAO>? = body?.let { Klaxon().parseArray(it) }

        assertEquals(true, parsedList!!.contains(newLecture1))
        assertEquals(true, parsedList!!.contains(newLecture2))
    }

    @Test
    fun `Create a new lecture and request it`() {
        val lectureData = LectureDAO(0, "Softwaretechnik I", firstMajor.majorID)
        val newLecture = post<LectureDAO, LectureDAO>("/majors/${firstMajor.majorID}/lectures", lectureData, trt, port)
        val fetchedLecture = get<LectureDAO>("/lectures/" + newLecture.lectureID, trt, port)

        assertEquals(newLecture, fetchedLecture)
        assertEquals(lectureData.name, fetchedLecture.name)
        assertNotEquals(lectureData.lectureID, fetchedLecture.lectureID)
    }

    @Test
    fun `Delete a created lecture`() {
        val lectureData = LectureDAO(0, "Programmieren I", firstMajor.majorID)
        val newLecture = post<LectureDAO, LectureDAO>("/majors/${firstMajor.majorID}/lectures", lectureData, trt, port)

        delete("/majors/${firstMajor.majorID}/lectures/${newLecture.lectureID}", trt, port)
        val result = getEx("/lectures/${newLecture.lectureID}", trt, port)
        val body = getBody(result)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertEquals("", body)
    }

    @Test
    fun `Delete a non-existing lecture`() {
        val result = deleteEx("/majors/${firstMajor.majorID}/lectures/0", trt, port)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }
}
