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
        val group = createAGroup(trt, port)
        val session = SessionDAO(0,group.groupID,"Mathebau", Date().time+100,45)
        val response = post<SessionDAO, SessionDAO>("/groups/${group.groupID}/sessions", session, trt, port)
        val list = getEx("/groups/${group.groupID}/sessions", trt, port)
        Assertions.assertNotNull(list.body)
        Assertions.assertNotEquals("[]", list.body)
        val sessionList:List<SessionDAO>? = list.body?.let { Klaxon().parseArray(it) }
        assertTrue(sessionList!!.contains(response))
    }

    @Test
    fun `Get a session by its ID`(){
        val session = createASession(trt, port)
        val response = get<SessionDAO>("/sessions/${session.sessionID}", trt, port) //get session
        assertEquals(session, response)
    }

    @Test
    fun `Get a session that does not exist`(){
        val response = getEx("/sessions/7654054", trt, port) //get session
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `Update a Session`(){
        val session = createASession(trt, port)
        val newSession = SessionDAO(session.sessionID, session.groupID, "another place", Date().time, 400)
        var response = put<SessionDAO,SessionDAO>("/sessions/${session.sessionID}", newSession, trt, port) //update session
        assertEquals(newSession, response)

        response = get("/sessions/${session.sessionID}", trt, port) //get session
        assertEquals(newSession, response)
    }

    @Test
    fun `Update a Session that does not exist`(){
        val session = createASession(trt, port)

        val newSession = SessionDAO(20450654, session.groupID, "another place3", Date().time, 40000)

        var response = putEx("/sessions/20450654", newSession, trt, port) //update fake session wrong sessionid
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val newSessionWrongGroup = SessionDAO(session.sessionID, 20654065, "another place2", Date().time, 40)

        response = putEx("/sessions/${session.sessionID}", newSessionWrongGroup, trt, port) //update fake session wrong groupid
        //assertEquals(HttpStatus.NOT_FOUND, response.statusCode) //TODO

        val newSessionWrongTime = SessionDAO(session.sessionID, session.groupID, "another place2", Date().time, -1565)

        response = putEx("/sessions/${session.sessionID}", newSessionWrongTime, trt, port) //update fake session wrong time
        //assertEquals(HttpStatus.NOT_FOUND, response.statusCode) //TODO
    }

    @Test
    fun `Delete a Session`(){
        val session = createASession(trt, port)
        var response = deleteEx("/sessions/${session.sessionID}",trt, port)
        assertEquals(HttpStatus.OK, response.statusCode)

        response = getEx("/sessions/${session.sessionID}",trt,port)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `Delete a Session that does not exist`(){
        var response = deleteEx("/sessions/685464",trt, port)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `set Participation in Session`(){
        val group = createAGroup(trt,port)
        val session = createASession(trt, port)

        //val response = put<>("/sessions/${session.sessionID}/participate/$user.",)
    }
}