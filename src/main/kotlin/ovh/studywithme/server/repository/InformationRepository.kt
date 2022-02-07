package ovh.studywithme.server.repository

import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.Major
import ovh.studywithme.server.model.Lecture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Repository
import javax.persistence.EntityManagerFactory
import javax.persistence.EntityManager
import javax.persistence.Persistence

@Repository
interface InstitutionRepository : JpaRepository<Institution, Long> {
    //fun getInstitutionsByString(searchString: String) : List<String>? {
        //var emfactory : EntityManagerFactory = Persistence.createEntityManagerFactory( "Eclpselink_JPA" )
        //var entitymanager: EntityManager = emfactory.createEntityManager()
        //val criteriaBuilder = entitymanager.criteriaBuilder
        //val criteriaQuery = criteriaBuilder.createQuery(Institution::class.java)

    //    return emptyList()
    //}
}

@Repository
interface MajorRepository : JpaRepository<Major, Long>

@Repository
interface LectureRepository : JpaRepository<Lecture, Long>