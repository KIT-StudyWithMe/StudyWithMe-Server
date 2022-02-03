package ovh.studywithme.server.controller

import ovh.studywithme.server.model.User
import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.repository.InstitutionRepository
import ovh.studywithme.server.repository.MajorRepository
import ovh.studywithme.server.repository.LectureRepository
import java.util.*
import org.springframework.stereotype.Service

@Service
public class InformationController(private val institutionRepository: InstitutionRepository, private val majorRepository: MajorRepository, private val lectureRepository: LectureRepository) : InformationControllerInterface {


    fun getAllInstitutions():List<Institution> {
        return institutionRepository.findAll()
    }

    fun getInstitutionsByName(searchName:String):List<Institution> {
        return emptyList()
    }

    fun createNewInstitution(institution:Institution) : Institution {
        return institutionRepository.save(institution)
    }

    fun deleteInstitution(institutionID:Long) : Boolean {
        val institution : Institution? = institutionRepository.findById(institutionID).unwrap()
        if (institution == null) {
            return false
        } else {
            institutionRepository.deleteById(institutionID)
            return true
        }
    }

    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}