package ovh.studywithme.server

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Assertions.assertEquals
import ovh.studywithme.server.repository.UserRepository
import ovh.studywithme.server.model.User

@DataJpaTest
@Tag("repository")
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository
    ) {

  @Test
  fun `When findByIdOrNull then return Article`() {
    println(">> Setup")
    val juergen = User(0, "juergen", 0, 0, "email", "testfirebaseID")
    entityManager.persist(juergen)
    entityManager.flush()
    //findByfirebaseUID
    val juergenFound: List<User> = userRepository.findByfirebaseUID("testfirebaseID")
    assertEquals(juergen.name, juergenFound.get(0).name)
  }
}