package ovh.studywithme.server

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StudyWithMeServerApplicationTests {

	@Test
	fun givenPerson_whenSaved_thenFound() {
		//HibernateCallback.doInHibernate(({ this.sessionFactory() }), { session ->
	  	//	val personToSave = Person(0, "John")
		//	session.persist(personToSave)
		//	val personFound = session.find(Person::class.java, personToSave.id)
		//	session.refresh(personFound)
	
		//	assertTrue(personToSave.name == personFound.name)
		//})
	}

}
