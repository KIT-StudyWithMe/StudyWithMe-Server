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
import ovh.studywithme.server.dao.UserDetailDAO
import ovh.studywithme.server.model.User

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Tag("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.Random::class)
class UserTests : RestTests(
) {

    val trt = TestRestTemplate()

    @LocalServerPort
    var port: Int = 0

    private var firstMajor = MajorDAO(0, "Maschinenbau")
    private var firstInstitution = InstitutionDAO(0, "TU München")

    @BeforeAll
    fun init() {
        firstMajor = post("/majors", firstMajor, trt, port)
        firstInstitution = post("/institutions", firstInstitution, trt, port)
    }

    @AfterAll
    fun tearDown() {
        delete("/majors/${firstMajor.majorID}", trt, port)
        delete("/institutions/${firstInstitution.institutionID}", trt, port)
    }

    //##############
    //# user tests #
    //##############

    @Test
    fun `Get user with ID 0 and expect nothing`() {
        val result = getEx("/users/0", trt, port)
        val body = getBody(result)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertEquals("", body)
    }

    @Test
    fun `Get user details with ID 0 and expect nothing`() {
        val result = getEx("/users/0/detail", trt, port)
        val body = getBody(result)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertEquals("", body)
    }

    @Test
    fun `Create a new user and request him`() {
        val userData = UserDetailDAO(0, "Michael Meier", firstInstitution.institutionID, firstInstitution.name,
            firstMajor.majorID, firstMajor.name, "michael.meier@student.tum.edu", "f1r3B4s31D", false)
        val newUser = post<UserDetailDAO, UserDetailDAO>("/users", userData, trt, port)
        val fetchedUser = get<UserDetailDAO>("/users/${newUser.userID}/detail", trt, port)

        assertEquals(newUser, fetchedUser)
        assertEquals(userData.name, fetchedUser.name)
        assertEquals(userData.institutionID, fetchedUser.institutionID)
        assertEquals(userData.institutionName, fetchedUser.institutionName)
        assertEquals(userData.majorID, fetchedUser.majorID)
        assertEquals(userData.majorName, fetchedUser.majorName)
        assertEquals(userData.contact, fetchedUser.contact)
        assertEquals(userData.isModerator, fetchedUser.isModerator)
        assertNotEquals(userData.userID, fetchedUser.userID)
    }

    @Test
    fun `Delete a created user`() {
        val userData = UserDetailDAO(0, "Erik Burger", firstInstitution.institutionID, firstInstitution.name,
            firstMajor.majorID, firstMajor.name, "erik.koch@student.tum.edu", "f1r3b453u1d70k3n", false)
        val newUser = post<UserDetailDAO, UserDetailDAO>("/users", userData, trt, port)

        delete("/users/${newUser.userID}", trt, port)

        val result = getEx("/users/${newUser.userID}", trt, port)
        val body = getBody(result)
        val resultDetail = getEx("/users/${newUser.userID}/detail", trt, port)
        val bodyDetail = getBody(resultDetail)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertEquals("", body)
        assertEquals(HttpStatus.NOT_FOUND, resultDetail.statusCode)
        assertEquals("", bodyDetail)
    }

    @Test
    fun `Delete a non-existing user`() {
        val result = deleteEx("/users/0", trt, port)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    @Test
    fun `Block a user`() {
        val userData = UserDetailDAO(0, "Jan Keim", firstInstitution.institutionID, firstInstitution.name,
            firstMajor.majorID, firstMajor.name, "jan.keim@student.tum.edu", "f1r3b453u1d70k3n", false)
        val newUser = post<UserDetailDAO, UserDetailDAO>("/users", userData, trt, port)

        val moderatorData = UserDetailDAO(0, "Sandro Koch", firstInstitution.institutionID, firstInstitution.name,
            firstMajor.majorID, firstMajor.name, "sandro.koch@student.tum.edu", "4n07h3ru1d", true)
        val newModerator = post<UserDetailDAO, UserDetailDAO>("/users", moderatorData, trt, port)

        val result = putEx("/users/${newUser.userID}/state/${newModerator.userID}", "", trt, port)

        val fetchedUser = get<UserDetailDAO>("/users/${newUser.userID}/detail", trt, port)
        val fetchedMod = get<UserDetailDAO>("/users/${newModerator.userID}/detail", trt, port)

        assertEquals(HttpStatus.OK, result.statusCode)

        assertEquals(newUser.userID, fetchedUser.userID)
        assertEquals(userData.name, fetchedUser.name)
        assertEquals(userData.institutionID, fetchedUser.institutionID)
        assertEquals(userData.institutionName, fetchedUser.institutionName)
        assertEquals(userData.majorID, fetchedUser.majorID)
        assertEquals(userData.majorName, fetchedUser.majorName)
        assertEquals(userData.contact, fetchedUser.contact)
        assertEquals(userData.isModerator, fetchedUser.isModerator)

        assertEquals(newModerator.userID, fetchedMod.userID)
        assertEquals(moderatorData.name, fetchedMod.name)
        assertEquals(moderatorData.institutionID, fetchedMod.institutionID)
        assertEquals(moderatorData.institutionName, fetchedMod.institutionName)
        assertEquals(moderatorData.majorID, fetchedMod.majorID)
        assertEquals(moderatorData.majorName, fetchedMod.majorName)
        assertEquals(moderatorData.contact, fetchedMod.contact)
        assertEquals(moderatorData.isModerator, fetchedMod.isModerator)
    }

    @Test
    fun `Get a not existing user's groups`() {
        val result = getEx("/users/0/groups", trt, port)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    @Test
    fun `Get a user's groups when he is in none`() {
        val userData = UserDetailDAO(0, "Yves Kirschner", firstInstitution.institutionID, firstInstitution.name,
            firstMajor.majorID, firstMajor.name, "yves.kirschner@student.tum.edu", "y374n07h3r0f7h353", false)
        val newUser = post<UserDetailDAO, UserDetailDAO>("/users", userData, trt, port)

        val fetchedGroups = get<InstitutionDAO>("/institutions/" + newInstitution.institutionID, trt, port)
        val result = getEx("/users/${newUser.userID}", trt, port)
        val body = getBody(result)
        val resultDetail = getEx("/users/${newUser.userID}/detail", trt, port)
        val bodyDetail = getBody(resultDetail)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertEquals("", body)
        assertEquals(HttpStatus.NOT_FOUND, resultDetail.statusCode)
        assertEquals("", bodyDetail)
    }

    /*@Test
    fun `Get a user's groups`() {
        val userData = UserDetailDAO(0, "Yves Kirschner", firstInstitution.institutionID, firstInstitution.name,
            firstMajor.majorID, firstMajor.name, "yves.kirschner@student.tum.edu", "y374n07h3r0f7h353", false)
        val newUser = post<UserDetailDAO, UserDetailDAO>("/users", userData, trt, port)

        //post("/groups/${newUser.userID}", "", trt, port)

        val result = getEx("/users/${newUser.userID}", trt, port)
        val body = getBody(result)
        val resultDetail = getEx("/users/${newUser.userID}/detail", trt, port)
        val bodyDetail = getBody(resultDetail)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals("", body)
        assertEquals(HttpStatus.OK, resultDetail.statusCode)
        assertEquals("", bodyDetail)
    }*/
}
