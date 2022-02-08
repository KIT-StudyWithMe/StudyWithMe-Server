package ovh.studywithme.server.controller

import ovh.studywithme.server.model.User
import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.Major
import ovh.studywithme.server.model.Lecture
import ovh.studywithme.server.repository.InstitutionRepository
import ovh.studywithme.server.repository.MajorRepository
import ovh.studywithme.server.repository.LectureRepository
import java.util.*
import org.springframework.stereotype.Service

@Service
    class InformationController(private val institutionRepository: InstitutionRepository, private val majorRepository: MajorRepository, private val lectureRepository: LectureRepository) : InformationControllerInterface {


    fun getAllInstitutions():List<Institution> {
        return institutionRepository.findAll()
    }

    fun getInstitutionByID(institutionID:Long):Institution? {
        return institutionRepository.findById(institutionID).unwrap()
    }    

    fun getInstitutionsByName(searchName:String):List<Institution> {
        //TODO implement search
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

    fun getAllMajors():List<Major> {
        return majorRepository.findAll()
    }

    fun getMajorByID(majorID:Long):Major? {
        return majorRepository.findById(majorID).unwrap()
    }  

    fun getMajorsByName(searchName:String):List<Major> {
        //TODO implement search
        return emptyList()
    }

    fun createNewMajor(major:Major) : Major {
        return majorRepository.save(major)
    }

    fun deleteMajor(majorID:Long) : Boolean {
        val major : Major? = majorRepository.findById(majorID).unwrap()
        if (major == null) {
            return false
        } else {
            majorRepository.deleteById(majorID)
            return true
        }
    }

    fun getAllLectures(majorID: Long):List<Lecture> {
        return lectureRepository.findAll()
    }

    fun getLectureByID(majorID: Long, lectureID:Long):Lecture? {
        return lectureRepository.findById(lectureID).unwrap()
    }  

    fun getLecturesByName(majorID: Long, searchName:String):List<Lecture> {
        //TODO implement search
        return emptyList()
    }

    fun createNewLecture(majorID: Long, lecture:Lecture) : Lecture {
        return lectureRepository.save(lecture)
    }

    fun deleteLecture(majorID: Long, lectureID:Long) : Boolean {
        val lecture : Lecture? = lectureRepository.findById(lectureID).unwrap()
        if (lecture == null) {
            return false
        } else {
            lectureRepository.deleteById(lectureID)
            return true
        }
    }

    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}