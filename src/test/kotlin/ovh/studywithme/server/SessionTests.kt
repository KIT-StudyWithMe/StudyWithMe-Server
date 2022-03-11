package ovh.studywithme.server

import com.beust.klaxon.Klaxon
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import ovh.studywithme.server.dao.*
import ovh.studywithme.server.model.SessionFrequency
import ovh.studywithme.server.model.SessionMode
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integration")
@TestMethodOrder(MethodOrderer.Random::class)
class SessionTests:RestTests() {
    val trt = TestRestTemplate()

    @LocalServerPort
    var port: Int = 0

    @Test
    fun `Create a session and get it`() {
        val group = createAGroup()
        val session = SessionDAO(0,group.groupID,"Mathebau", Date().time+100,45)
        val response = post<SessionDAO, SessionDAO>("/groups/${group.groupID}/sessions", session, trt, port)
        val list = getEx("/groups/${group.groupID}/sessions", trt, port)
        Assertions.assertNotNull(list.body)
        Assertions.assertNotEquals("[]", list.body)
        val sessionList:List<SessionDAO>? = list.body?.let { Klaxon().parseArray(it) }
        assertTrue(sessionList!!.contains(response))
    }

    fun createAGroup() : StudyGroupDAO {
        val inst = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FHKA"), trt, port)
        val major = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Info"), trt, port)
        val lecture = post<LectureDAO, LectureDAO>("/majors/${major.majorID}/lectures", LectureDAO(0,"PSE",major.majorID), trt, port)
        val user = UserDetailDAO(0, "Hans", inst.institutionID, inst.name, major.majorID, major.name, "email@test.de","FHEASTH",false)

        val group = StudyGroupDAO(0,"Beste Lerngruppe?","Die coolsten!!",lecture.lectureID,
            SessionFrequency.ONCE,
            SessionMode.PRESENCE,3,100000,15)
        return post<StudyGroupDAO, StudyGroupDAO>("/groups/${user.userID}",group,trt,port)
    }
}