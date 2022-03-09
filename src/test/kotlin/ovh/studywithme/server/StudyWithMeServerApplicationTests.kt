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
import ovh.studywithme.server.dao.UserDetailDAO
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
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Tag("integration")
@TestMethodOrder(OrderAnnotation::class)
class StudyWithMeServerApplicationTests : RestTests(
	) {

	var trt = TestRestTemplate()

	@LocalServerPort
  	var port: Int = 0

	  @BeforeEach
	  fun wipeDB() {
	  }

	  @Order(1)
	  @Test
	  fun getNonexistentInstitution() {
		val result = trt.exchange(
			URI("http://localhost:" + port + "/institutions/0"),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)
	  
		Assertions.assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
	  }

	  @Order(2)
	  @Test
	  fun getAllInstitutions() {
		val result = trt.exchange(
			URI("http://localhost:" + port + "/institutions"),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)
	  
		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
	  }

	  @Order(3)
	  @Test
	  fun createInstitution() {
		val result = trt.exchange(
			URI("http://localhost:" + port + "/institutions"),
			HttpMethod.POST,
			HttpEntity(Institution(0, "testInstitution")),
			String::class.java)
	  
		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
	  }

	  @Order(4)
	  @Test
	  fun findInstitution() {
		trt.exchange(
			URI("http://localhost:" + port + "/institutions"),
			HttpMethod.POST,
			HttpEntity(Institution(0, "testInstitutionTEEESST")),
			String::class.java)
		
		val result = trt.exchange(
			URI("http://localhost:$port/institutions?name=testInstitutionTEEESST"),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)

		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
	  }

	  @Order(5)
	  @Test
	  fun reportGroup() {
		trt.exchange(
			URI("http://localhost:" + port + "/groups/1/report/1"),
			HttpMethod.PUT,
			HttpEntity(StudyGroupField.NAME),
			String::class.java)
		
		val result = trt.exchange(
			URI("http://localhost:" + port + "/reports/group"),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)

		val test:List<UserDetailDAO> = get("/users", trt, port)
		val test2:UserDetailDAO = post("/users", UserDetailDAO(0,"test",0,"KIT",0,"Informatik","email","adsf", false), trt, port)
		//logger.info(test[0].name)

		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
	  }
}
