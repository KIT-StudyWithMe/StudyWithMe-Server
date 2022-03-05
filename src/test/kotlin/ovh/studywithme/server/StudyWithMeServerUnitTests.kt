package ovh.studywithme.servertests

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.ActiveProfiles
import org.springframework.http.HttpStatus
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.beans.factory.annotation.Autowired
import ovh.studywithme.server.model.User
import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.StudyGroupField
import ovh.studywithme.server.model.StudyGroupReport
import ovh.studywithme.server.controller.UserController
import ovh.studywithme.server.controller.GroupController
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
import ovh.studywithme.server.view.UserView
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.hibernate.annotations.NotFound
//import io.mockk.every
import com.ninjasquad.springmockk.MockkBean
import java.net.URI

@ExtendWith(SpringExtension::class)
//@WebMvcTest
@SpringBootTest(
	//classes = "StudyWithMeServerApplication"
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
//@WebMvcTest(UserView::class)
//@ActiveProfiles(value = ["unittest"])
internal class StudyWithMeServerUnitTests(
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

	//@Autowired
	//private lateinit var userView: UserView

	//@MockkBean
	//private lateinit var groupController: GroupController

	//@TestConfiguration
  	//internal class GroupController {
    //@Bean
    //	fun deleteUserFromGroup(): Boolean = mockk()
  	//}
	
	@Test
	fun reportGroup() {
		//every { fooClient.isOpen() } returns true
		//every { groupController.deleteUserFromGroup(1, 2) } returns true
		//Assertions.assertEquals((groupController.deleteUserFromGroup(1, 2),true)
		Assertions.assertEquals(1,1)
	}
}
