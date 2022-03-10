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
import org.springframework.core.ParameterizedTypeReference
import ovh.studywithme.server.dao.*
import ovh.studywithme.server.model.SessionFrequency
import ovh.studywithme.server.model.SessionMode
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
    private var firstLecture = LectureDAO(0, "Werkstoffkunde I", firstMajor.majorID)

    @BeforeAll
    fun init() {
        firstMajor = post("/majors", firstMajor, trt, port)
        firstInstitution = post("/institutions", firstInstitution, trt, port)
        firstLecture = post("/majors/${firstMajor.majorID}/lectures", firstLecture, trt, port)
    }

    @AfterAll
    fun tearDown() {
        delete("/majors/${firstMajor.majorID}", trt, port)
        delete("/institutions/${firstInstitution.institutionID}", trt, port)
        delete("/majors/${firstMajor.majorID}/lectures/${firstLecture.lectureID}", trt, port)
    }


    @Test
    fun `Get complete user list`() {
        val userData1 = UserDetailDAO(
            0, "Anne Koziolek", firstInstitution.institutionID, firstInstitution.name,
            firstMajor.majorID, firstMajor.name, "anne.koziolek@ket.edu", "pr06r4mm13r3nw4rk4353", false
        )
        val newUser1 = post<UserDetailDAO, UserDetailDAO>("/users", userData1, trt, port)

        val userData2 = UserDetailDAO(
            0, "Ralf Reussner", firstInstitution.institutionID, firstInstitution.name,
            firstMajor.majorID, firstMajor.name, "ralf.reussner@ket.edu", "mp1w4r1n73r3554n7", true
        )
        val newUser2 = post<UserDetailDAO, UserDetailDAO>("/users", userData2, trt, port)

        val userData3 = UserDetailDAO(
            0, "Sabine Beyer", firstInstitution.institutionID, firstInstitution.name,
            firstMajor.majorID, firstMajor.name, "sabine.beyer@ket.edu", "1chk3nn3d1364rn1ch7", false
        )
        val newUser3 = post<UserDetailDAO, UserDetailDAO>("/users", userData2, trt, port)
        deleteEx("/users/${newUser3.userID}", trt, port)

        val fetchedUsers = getEx("/users", trt, port)
        val body : String? = fetchedUsers.body
        assertNotNull(body)
        val parsedList:List<UserDAO>? = body?.let { Klaxon().parseArray(it) }

        assertEquals(true, parsedList!!.contains(UserDAO(newUser1.toUser())))
        assertEquals(true, parsedList.contains(UserDAO(newUser2.toUser())))
        assertEquals(false, parsedList.contains(UserDAO(newUser3.toUser())))
    }

    @Test
    fun `Get user with ID 0 and expect nothing`() {
        val result = getEx("/users/0", trt, port)
        val body = getBody(result)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertEquals("", body)
    }

    @Test
    fun `Get detailed user with ID 0 and expect nothing`() {
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

        val fetchedGroups = get<List<StudyGroupDAO>>("/users/${newUser.userID}/groups", trt, port)

        assertEquals(0, fetchedGroups.size)
    }

    @Test
    fun `Get a user's groups`() {
        val userData = UserDetailDAO(0, "Gregor Snelting", firstInstitution.institutionID, firstInstitution.name,
            firstMajor.majorID, firstMajor.name, "gregor.snelting@fernuni-hagen.edu", "5ch03n3n6u73n746", true)
        val newUser = post<UserDetailDAO, UserDetailDAO>("/users", userData, trt, port)

        var anotherLecture = LectureDAO(0, "Mechanik II", firstMajor.majorID)
        anotherLecture = post("/majors/${firstMajor.majorID}/lectures", anotherLecture, trt, port)

        val group1Data = StudyGroupDAO(0, "Team Elite", "KIT - Das E steht für Elite", firstLecture.lectureID,
            SessionFrequency.WEEKLY, SessionMode.PRESENCE, 6, 3, 1)
        val group2Data = StudyGroupDAO(0, "Team NotSoElite", "Wer sein Studium liebt, der schiebt",
            anotherLecture.lectureID, SessionFrequency.WEEKLY, SessionMode.ONLINE, 11, 5, 1)

        val newGroup1 = post<StudyGroupDAO, StudyGroupDAO>("/groups/${newUser.userID}", group1Data, trt, port)
        val newGroup2 = post<StudyGroupDAO, StudyGroupDAO>("/groups/${newUser.userID}", group2Data, trt, port)

        val fetchedGroups = getEx("/users/${newUser.userID}/groups", trt, port)

        val body : String? = fetchedGroups.body
        assertNotNull(body)
        val parsedList:List<StudyGroupDAO>? = body?.let { Klaxon().parseArray<StudyGroupDAO>(it) }

        assertEquals(2, parsedList!!.size)
        assertEquals(true, parsedList.contains(newGroup1))
        assertEquals(true, parsedList.contains(newGroup2))
    }
}
