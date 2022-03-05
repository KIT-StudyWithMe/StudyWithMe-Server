package ovh.studywithme.servertests

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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.hibernate.annotations.NotFound
import java.net.URI

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
//@ActiveProfiles(value = ["integrationtest"])
class StudyWithMeServerIntegrationTests(
	//private val groupRepository: GroupRepository,
	//private val groupMemberRepository: GroupMemberRepository,

	//private val institutionRepository: InstitutionRepository,
	//private val majorRepository: MajorRepository,
	//private val lectureRepository: LectureRepository,

	//private val userReportRepository: UserReportRepository,
	//private val groupReportRepository: GroupReportRepository,
	//private val sessionReportRepository: SessionReportRepository,

	//private val attendeeRepository: AttendeeRepository,
	//private val sessionRepository: SessionRepository,
	
	//private val userRepository: UserRepository
	) {

	var testRestTemplate = TestRestTemplate()

	//@LocalServerPort
  	var serverPort: Int = 12808


	  @BeforeEach
	  fun wipeDB() {
		  //groupRepository.deleteAll()
		  //groupMemberRepository.deleteAll()

		  //institutionRepository.deleteAll()
		  //majorRepository.deleteAll()
		  //lectureRepository.deleteAll()

		  //userReportRepository.deleteAll()
		  //groupReportRepository.deleteAll()
		  //sessionReportRepository.deleteAll()

		  //attendeeRepository.deleteAll()
		  //sessionRepository.deleteAll()

		  //userRepository.deleteAll()
	  }

	  @Test
	  fun getNonexistentInstitution() {
		val result = testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + "/institutions/0"),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)
	  
		Assertions.assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
	  }

	  @Test
	  fun getAllInstitutions() {
		val result = testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + "/institutions"),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)
	  
		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
	  }

	  @Test
	  fun createInstitution() {
		val result = testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + "/institutions"),
			HttpMethod.POST,
			HttpEntity(Institution(0, "testInstitution")),
			String::class.java)
	  
		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
	  }

	  @Test
	  fun findInstitution() {
		testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + "/institutions"),
			HttpMethod.POST,
			HttpEntity(Institution(0, "testInstitutionTEEESST")),
			String::class.java)
		
		val result = testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + "/institutions?name=testInstitutionTEEESST"),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)

		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
	  }

	  @Test
	  fun reportGroup() {
		testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + "/groups/1/report/1"),
			HttpMethod.PUT,
			HttpEntity(StudyGroupField.NAME),
			String::class.java)
		
		val result = testRestTemplate.exchange(
			URI("http://localhost:" + serverPort + "/reports/group"),
			HttpMethod.GET,
			HttpEntity(""),
			String::class.java)

		Assertions.assertEquals(HttpStatus.OK, result.statusCode)
	  }



	  

	//@Test
	//fun repositoryWhenSaved_thenFindsByID(userRepository: UserRepository) {
	//	val user :User = userRepository.save(User(0, "Zaphod Beeblebrox", 0, 0, "thisismymail@test.com", "LALALALALA"))
  	//	assertThat(userRepository.findById(user.userID)).isNotNull()
	//}

	//@Test
	//fun controllerWhenSaved_thenFindsByID(userController: UserController) {
	//	userController.createUser(User(0, "Zaphod Beeblebrox", 0, 0, "thisismymail@test.com", "LALALALALA"))
  	//	assertThat(userController.getAllUsers()).isNotNull()
	//}
}
