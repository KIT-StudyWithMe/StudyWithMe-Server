package ovh.studywithme.server

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import ovh.studywithme.server.model.User
import ovh.studywithme.server.controller.UserController
import ovh.studywithme.server.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat

//@SpringBootTest
@DataJpaTest
class StudyWithMeServerApplicationTests {

	@Test
	fun repositoryWhenSaved_thenFindsByID(userRepository: UserRepository) {
		val user :User = userRepository.save(User(0, "Zaphod Beeblebrox", 0, 0, "thisismymail@test.com", "LALALALALA"))
  		assertThat(userRepository.findById(user.userID)).isNotNull()
	}

	@Test
	fun controllerWhenSaved_thenFindsByID(userController: UserController) {
		userController.createUser(User(0, "Zaphod Beeblebrox", 0, 0, "thisismymail@test.com", "LALALALALA"))
  		assertThat(userController.getAllUsers()).isNotNull()
	}
}
