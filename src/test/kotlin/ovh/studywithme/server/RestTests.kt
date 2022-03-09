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
@Tag("integration")
open class RestTests(){

	//val logger = LoggerFactory.getLogger(this.javaClass);

    final inline fun <reified T : Any> get(path:String, trt:TestRestTemplate, port:Int): T {
		return trt.getForObject<T>(
			URI("http://localhost:" + port + path),
			T::class.java)
	}


    final inline fun <S,reified T : Any> post(path:String, payload:S, trt:TestRestTemplate, port:Int): T {
		return trt.postForObject<T>(
			URI("http://localhost:" + port + path),
            HttpEntity(payload),
			T::class.java)
	}
}
