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
import ovh.studywithme.server.model.SessionFrequency
import ovh.studywithme.server.model.SessionMode
import java.net.URI

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

	fun createAGroup(trt:TestRestTemplate, port:Int) : StudyGroupDAO {
		val inst = post<InstitutionDAO, InstitutionDAO>("/institutions", InstitutionDAO(0, "FHKA"), trt, port) //create a Instutution
		val major = post<MajorDAO, MajorDAO>("/majors", MajorDAO(0, "Info"), trt, port) //create a Major
		val lecture = post<LectureDAO, LectureDAO>("/majors/${major.majorID}/lectures", LectureDAO(0,"PSE",major.majorID), trt, port) //create a Lecture
		var user = UserDetailDAO(0, "Hans", inst.institutionID, inst.name, major.majorID, major.name, "email@test.de","FHEASTH",false)
		user = post("/users",user,trt,port) //create user
		val group = StudyGroupDAO(0,"Beste Lerngruppe?","Die coolsten!!",lecture.lectureID,
			SessionFrequency.ONCE,
			SessionMode.PRESENCE,3,100000,15)
		return post<StudyGroupDAO, StudyGroupDAO>("/groups/${user.userID}",group,trt,port) //create group
	}
}