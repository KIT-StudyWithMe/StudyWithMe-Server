package ovh.studywithme.server

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.http.HttpStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.MethodOrderer
import ovh.studywithme.server.dao.InstitutionDAO
import ovh.studywithme.server.dao.MajorDAO

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Tag("integration")
@TestMethodOrder(MethodOrderer.Random::class)
class InformationTests : RestTests(
) {

    val trt = TestRestTemplate()

    @LocalServerPort
    var port: Int = 0

    @Test
    fun `Get institution with ID 0 and expect nothing`() {
        val result = getEx("/institutions/0", trt, port)
        val body = getBody(result)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        Assertions.assertEquals("", body)
    }
/*
    @Test
    fun `Create some institutions and verify the list of all institutions contains them`() {
        val newInstitution1 = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FH Karlsruhe"), trt, port)
        val newInstitution2 = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "Fernuni Bremen"), trt, port)

        val fetchedInstitutions = get<List<InstitutionDAO>>("/institutions", trt, port)
        //val temp = fetchedInstitutions.get(0)

        //Assertions.assertEquals(newInstitution1, temp)
        //Assertions.assertEquals(true, fetchedInstitutions.map { it.institutionID }.contains(newInstitution2.institutionID))
    }
*/
    @Test
    fun `Create a new institution and request it`() {
        val newInstitution = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "KIT"), trt, port)
        val fetchedInstitution = get<InstitutionDAO>("/institutions/" + newInstitution.institutionID, trt, port)

        Assertions.assertEquals(newInstitution, fetchedInstitution)
        Assertions.assertEquals("KIT", fetchedInstitution.name)
    }

    @Test
    fun `Delete a created institution`() {
        val newInstitution = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "Fernuni Hagen"), trt, port)

        delete("/institutions/${newInstitution.institutionID}", trt, port)
        val result = getEx("/institutions/${newInstitution.institutionID}", trt, port)
        val body = getBody(result)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        Assertions.assertEquals("", body)
    }

    @Test
    fun `Delete a non-existing institution`() {
        val result = deleteEx("/institutions/0", trt, port)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    @Test
    fun `Get major with ID 0 and expect nothing`() {
        val result = getEx("/majors/0", trt, port)
        val body = getBody(result)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        Assertions.assertEquals("", body)
    }

    /*@Test
    fun `Create some majors and verify the list of all majors contains them`() {
        val newMajor1 = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Chemie"), trt, port)
        val newMajor2 = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Biologie"), trt, port)

        val fetchedMajors = get<List<MajorDAO>>("/majors", trt, port)

        Assertions.assertEquals(true, fetchedMajors.map { it.majorID }.contains(newMajor1.majorID))
        Assertions.assertEquals(true, fetchedMajors.map { it.majorID }.contains(newMajor2.majorID))
    }*/

    @Test
    fun `Create a new major and request it`() {
        val newMajor = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Informatik"), trt, port)
        val fetchedMajor = get<MajorDAO>("/majors/" + newMajor.majorID, trt, port)

        Assertions.assertEquals(newMajor, fetchedMajor)
        Assertions.assertEquals("Informatik", fetchedMajor.name)
    }

    @Test
    fun `Delete a created major`() {
        val newMajor = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Mandala Informatik"), trt, port)

        delete("/majors/${newMajor.majorID}", trt, port)
        val result = getEx("/majors/${newMajor.majorID}", trt, port)
        val body = getBody(result)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        Assertions.assertEquals("", body)
    }

    @Test
    fun `Delete a non-existing major`() {
        val result = deleteEx("/majors/0", trt, port)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }
}
