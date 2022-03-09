package ovh.studywithme.server

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.http.HttpStatus
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import ovh.studywithme.server.dao.UserDetailDAO
import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.StudyGroupField
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.BeforeEach
import java.net.URI

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
