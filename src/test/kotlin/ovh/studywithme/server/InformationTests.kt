package ovh.studywithme.server

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
import org.springframework.core.ParameterizedTypeReference
import ovh.studywithme.server.dao.InstitutionDAO
import ovh.studywithme.server.dao.LectureDAO
import ovh.studywithme.server.dao.MajorDAO

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Tag("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Random::class)
class InformationTests : RestTests(
) {

    val trt = TestRestTemplate()

    @LocalServerPort
    var port: Int = 0

    private var firstMajor = MajorDAO(0, "Gender Studies")

    @BeforeAll
    fun init() {
        firstMajor = post("/majors", firstMajor, trt, port)
    }

    @AfterAll
    fun tearDown() {
        delete("/majors/${firstMajor.majorID}", trt, port)
    }

    //#####################
    //# institution tests #
    //#####################

    @Test
    fun `Get institution with ID 0 and expect nothing`() {
        val result = getEx("/institutions/0", trt, port)
        val body = getBody(result)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertEquals("", body)
    }

    //@Test
    fun `Create some institutions and verify the list of all institutions contains them`() {
        //TODO fix this mess
        val newInstitution1 = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FH Karlsruhe"), trt, port)
        val newInstitution2 = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "Fernuni Bremen"), trt, port)

        val testType : ParameterizedTypeReference<List<InstitutionDAO>>
        val fetchedInstitutions = get<List<InstitutionDAO>>("/institutions", trt, port)
        println("XXXXXXXXXXXXXXX\n${fetchedInstitutions.first()}\n")
        println("XXXXXXXXXXXXXXX\n${fetchedInstitutions[1]}\n")

        val test = fetchedInstitutions.first()

        //val institutionIDs : List<Long> = fetchedInstitutions.map{ it.institutionID }

        //Assertions.assertEquals(true, fetchedInstitutions.map { it.institutionID}.toMutableList().contains(newInstitution1.institutionID))
        //Assertions.assertEquals(true, fetchedInstitutions.map { it.institutionID }.contains(newInstitution2.institutionID))
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

    //@Test
    fun `Create some majors and verify the list of all majors contains them`() {
        //TODO
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

    //@Test
    fun `Create some lectures and verify the list of all majors contains them`() {
        //TODO
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
