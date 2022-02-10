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
import ovh.studywithme.server.model.User
import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.controller.UserController
import ovh.studywithme.server.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.hibernate.annotations.NotFound
import java.net.URI

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(value = ["test"])
class StudyWithMeServerApplicationTests() {

	var testRestTemplate = TestRestTemplate()

	@LocalServerPort
  	var serverPort: Int = 8080

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
