package ovh.studywithme.server

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.ActiveProfiles
import org.springframework.http.HttpStatus
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import ovh.studywithme.server.model.User
import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.StudyGroupField
import ovh.studywithme.server.model.StudyGroupReport
import ovh.studywithme.server.controller.UserController
import ovh.studywithme.server.repository.UserRepository
import ovh.studywithme.server.repository.GroupRepository
import ovh.studywithme.server.repository.GroupMemberRepository
import ovh.studywithme.server.repository.GroupReportRepository
import ovh.studywithme.server.repository.LectureRepository
import ovh.studywithme.server.repository.UserReportRepository
import ovh.studywithme.server.repository.SessionReportRepository
import ovh.studywithme.server.repository.InstitutionRepository
import ovh.studywithme.server.repository.MajorRepository
import ovh.studywithme.server.repository.AttendeeRepository
import ovh.studywithme.server.repository.SessionRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.hibernate.annotations.NotFound
import java.net.URI
import io.mockk.mockk
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Tag("integration")
@TestMethodOrder(OrderAnnotation::class)
class InstitutionTests() {
	var testRestTemplate = TestRestTemplate()
	val mapper = jacksonObjectMapper()

	@LocalServerPort
  	var serverPort: Int = 0

	  @Order(1)
	  @Test
	  fun getNonexistentInstitution() {
		val result = testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + "/institutions/0"),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)
	  
		Assertions.assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
	  }

	  @Order(2)
	  @Test
	  fun getAllInstitutions() {
		val result = testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + "/institutions"),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)
	  
		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
	  }

	  @Order(3)
	  @Test
	  fun createInstitution() {
		val result = testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + "/institutions"),
			HttpMethod.POST,
			HttpEntity(Institution(0, "testInstitution")),
			String::class.java)
	  
		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
	  }

	  @Order(4)
	  @Test
	  fun findInstitution() {
		val institution = post("/institutions", Institution(0, "testInstitutionTEEESST"))

		val answer = get("/institutions?name=testInstitutionTEEESST")

		Assertions.assertEquals(institution.getBody(), answer.getBody())
	  }

	  @Order(5)
	  @Test
	  fun reportGroup() {
		val create = putBody("/groups/1/report/1",StudyGroupField.NAME)
		
		val result = getBody("/reports/group")

		var institute: Institution = mapper.readValue(create)
		var instituteResult: Institution = mapper.readValue(result)

		//todo assert not null
		Assertions.assertEquals(institute, instituteResult)
	  }

	  //helper methods
	  fun get(path:String):ResponseEntity<String!>!{
		return testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + path),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)
	  }

	  fun getBody(path:String):String{
		  val output: String? = get(path).getBody()
		  if (output!=null){
			  return output
		  }
		  return ""
	  }

	  fun <T> put(path:String, object:T):ResponseEntity<String!>!{
		return testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + path),
			HttpMethod.PUT,
			HttpEntity(object),
			String::class.java)
	  }

	fun <T> putBody(path:String, object:T):String{
		val output: String? = put(path, object).getBody()
		if (output!=null){
			return output
		}
		return ""
	}

	fun <T> post(path:String, object:T):ResponseEntity<String!>!{
		return testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + path),
			HttpMethod.POST,
			HttpEntity(object),
			String::class.java)
	}

	fun <T> postBody(path:String, object:T):String{
		val output: String? = post(path, object).getBody()
		if (output!=null){
			return output
		}
		return ""
	}
}
