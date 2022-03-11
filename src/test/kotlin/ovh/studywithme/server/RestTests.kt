package ovh.studywithme.server

import com.beust.klaxon.Klaxon
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.http.HttpEntity
import org.junit.jupiter.api.Tag
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import ovh.studywithme.server.dao.*
import ovh.studywithme.server.model.GroupID
import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.SessionFrequency
import ovh.studywithme.server.model.SessionMode
import java.net.URI
import java.util.*

@ExtendWith(SpringExtension::class)
@Tag("integration")
open class RestTests(){

	val mapper = jacksonObjectMapper()

	inline fun <reified T : Any> get(path:String, trt:TestRestTemplate, port:Int): T {
		return trt.getForObject<T>(
			URI("http://localhost:" + port + path),
			T::class.java)
	}

	//todo maybe change that to the same code as put() to support lists. that makes this method nullable so we have to change much code
    inline fun <S,reified T : Any> post(path:String, payload:S, trt:TestRestTemplate, port:Int): T {
		return trt.postForObject<T>(
			URI("http://localhost:" + port + path),
            HttpEntity(payload),
			T::class.java)
	}

	inline fun <S,reified T> put(path:String, payload:S, trt:TestRestTemplate, port:Int):T? {
		val response = trt.exchange(
			URI("http://localhost:" + port + path),
			HttpMethod.PUT,
			HttpEntity(payload),
			String::class.java)
		return response.body?.let { Klaxon().parse(it) }
	}

    fun delete(path:String, trt:TestRestTemplate, port:Int) {
		return trt.delete(
			URI("http://localhost:" + port + path))
	}

    fun getBody(response: ResponseEntity<String>):String{
        val output: String? = response.body
        if (output!=null){
            return output
        }
        return ""
    }

	inline fun <reified T> getBodyObj(response: ResponseEntity<String>):T?{
		val output: String? = response.body
		if (output!=null){
			return mapper.readValue(output, T::class.java)
		}
		return null
	}

	fun getEx(path:String,trt:TestRestTemplate, port:Int): ResponseEntity<String> {
		return trt.exchange(
			URI("http://localhost:" + port + path),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)
	}

	fun <T> putEx(path:String, payload:T,trt:TestRestTemplate, port:Int):ResponseEntity<String>{
		return trt.exchange(
			URI("http://localhost:" + port + path),
			HttpMethod.PUT,
			HttpEntity(payload),
			String::class.java)
	}

	fun <T> postEx(path:String, payload:T,trt:TestRestTemplate, port:Int):ResponseEntity<String>{
		return trt.exchange(
			URI("http://localhost:" + port + path),
			HttpMethod.POST,
			HttpEntity(payload),
			String::class.java)
	}

	fun deleteEx(path:String,trt:TestRestTemplate, port:Int): ResponseEntity<String> {
		return trt.exchange(
			URI("http://localhost:" + port + path),
			HttpMethod.DELETE,
			HttpEntity(""),
			String::class.java)
	}

	// -- Group
	fun createAGroup(trt:TestRestTemplate, port:Int) : StudyGroupDAO {
		val inst = createAInstitution(trt, port)
		val major = createAMajor(trt, port)
		val lecture = createALecture(major, trt, port)
		val user = createAUser(inst, major, trt, port)
		return createAGroup(lecture, user, trt, port)
	}

	fun createAGroup(user:UserDetailDAO, trt:TestRestTemplate, port:Int) : StudyGroupDAO {
		val major:MajorDAO = get("/majors/${user.majorID}",trt,port)
		val lecture = createALecture(major, trt, port)
		return createAGroup(lecture, user, trt, port)
	}

	fun createAGroup(lecture: LectureDAO, user: UserDetailDAO, trt: TestRestTemplate, port: Int):StudyGroupDAO{
		val group = StudyGroupDAO(0,"Beste Lerngruppe?","Die coolsten!!",lecture.lectureID,
			SessionFrequency.ONCE,
			SessionMode.PRESENCE,3,10,0)
		return post<StudyGroupDAO, StudyGroupDAO>("/groups/${user.userID}",group,trt,port) //create group
	}

	// -- Institution
	fun createAInstitution(trt:TestRestTemplate, port:Int):InstitutionDAO{
		return post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FHKA"), trt, port) //create a Instutution
	}

	// -- Major
	fun createAMajor(trt:TestRestTemplate, port:Int):MajorDAO{
		return post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Info"), trt, port) //create a Major
	}

	// -- Lecture
	fun createALecture(trt: TestRestTemplate, port: Int):LectureDAO{
		val major = createAMajor(trt, port)
		return post<LectureDAO, LectureDAO>("/majors/${major.majorID}/lectures", LectureDAO(0,"PSE",major.majorID), trt, port) //create a Lecture
	}
	fun createALecture(major: MajorDAO, trt: TestRestTemplate, port: Int):LectureDAO{
		return post<LectureDAO, LectureDAO>("/majors/${major.majorID}/lectures", LectureDAO(0,"PSE",major.majorID), trt, port) //create a Lecture
	}

	// -- User
	fun createAUser(trt: TestRestTemplate, port: Int): UserDetailDAO{
		val inst = createAInstitution(trt, port)
		val major = createAMajor(trt, port)
		var user = createAUser(inst, major, trt, port)
		return post("/users",user,trt,port) //create user
	}

	fun createAUser(major:MajorDAO, trt: TestRestTemplate, port: Int): UserDetailDAO{
		val inst = createAInstitution(trt, port)
		var user = UserDetailDAO(
			0,
			"Hans",
			inst.institutionID,
			inst.name,
			major.majorID,
			major.name,
			"email@test.de",
			"FHEASTH",
			false)
		return post("/users",user,trt,port) //create user
	}

	fun createAUser(inst: InstitutionDAO, major:MajorDAO, trt: TestRestTemplate, port: Int): UserDetailDAO{
		var user = UserDetailDAO(
			0,
			"Hans",
			inst.institutionID,
			inst.name,
			major.majorID,
			major.name,
			"email@test.de",
			"FHEASTH",
			false)
		return post("/users",user,trt,port) //create user
	}

	// -- Session
	fun createASession(trt: TestRestTemplate, port: Int):SessionDAO {
		val group = createAGroup(trt, port)
		val session = SessionDAO(0,group.groupID,"Mathebau", Date().time+100,45)
		return post("/groups/${group.groupID}/sessions", session, trt, port) //create session
	}

	fun createASession(group: StudyGroupDAO, trt: TestRestTemplate, port: Int):SessionDAO {
		val session = SessionDAO(0,group.groupID,"Mathebau", Date().time+100,45)
		return post("/groups/${group.groupID}/sessions", session, trt, port) //create session
	}
}