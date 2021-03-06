package ovh.studywithme.server.controller

import ovh.studywithme.server.model.User
import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.Major
import ovh.studywithme.server.model.Lecture
import ovh.studywithme.server.model.LectureID
import ovh.studywithme.server.dao.InstitutionDAO
import ovh.studywithme.server.dao.LectureDAO
import ovh.studywithme.server.dao.MajorDAO
import ovh.studywithme.server.repository.InstitutionRepository
import ovh.studywithme.server.repository.MajorRepository
import ovh.studywithme.server.repository.LectureRepository
import java.util.*
import org.springframework.stereotype.Service
import ovh.studywithme.server.repository.UserRepository

/**
 * Implementation of the interface controller interface.
 *
 * @property institutionRepository
 * @property majorRepository
 * @property lectureRepository
 * @constructor Create an information controller, all variables are instanced by Spring's autowire
 */
@Service
    class InformationController(private val institutionRepository: InstitutionRepository,
                                private val majorRepository: MajorRepository,
                                private val userRepository: UserRepository,
                                private val lectureRepository: LectureRepository) : InformationControllerInterface {

    override fun getAllInstitutions():List<InstitutionDAO> {
        return institutionRepository.findAll().map{InstitutionDAO(it)}
    }

    override fun getInstitutionByID(institutionID:Long):InstitutionDAO? {
        val institution : Institution? = institutionRepository.findById(institutionID).unwrap()
        if (institution!=null) {
            return InstitutionDAO(institution)
        }
        return null
    }

    override fun getInstitutionsByName(searchName:String):List<InstitutionDAO> {
        return institutionRepository.findByNameStartsWith(searchName).map{InstitutionDAO(it)}
    }

    override fun createNewInstitution(institution:InstitutionDAO) : InstitutionDAO {
        return InstitutionDAO(institutionRepository.save(institution.toInstitution()))
    }

    override fun deleteInstitution(institutionID:Long) : Boolean {
        val institution : Institution? = institutionRepository.findById(institutionID).unwrap()
        if (institution == null) {
            return false
        } else {
            institutionRepository.deleteById(institutionID)
            return true
        }
    }

    override fun getAllMajors():List<MajorDAO> {
        return majorRepository.findAll().map{MajorDAO(it)}
    }

    override fun getMajorByID(majorID:Long):MajorDAO? {
        val major : Major? = majorRepository.findById(majorID).unwrap()
        if (major!=null) {
            return MajorDAO(major)
        }
        return null
    }

    override fun getMajorsByName(searchName:String):List<MajorDAO> {
        return majorRepository.findByNameStartsWith(searchName).map{MajorDAO(it)}
    }

    override fun createNewMajor(major:MajorDAO) : MajorDAO {
        return MajorDAO(majorRepository.save(major.toMajor()))
    }

    override fun deleteMajor(majorID:Long) : Boolean {
        val major : Major? = majorRepository.findById(majorID).unwrap()
        if (major == null) {
            return false
        } else {
            majorRepository.deleteById(majorID)
            return true
        }
    }

    override fun getAllLectures(majorID: Long):List<LectureDAO> {
        return lectureRepository.findAll().map{LectureDAO(it)}
    }

    override fun getLectureByID(lectureID:Long):LectureDAO? {
        val lecture : Lecture? = lectureRepository.findById(lectureID).unwrap()
        if (lecture!=null) {
            return LectureDAO(lecture)
        }
        return null
    }

    override fun getLecturesByName(majorID: Long, searchName:String):List<LectureDAO> {
        // If no valid majorID, return all Lectures that start with the searchName.
        if (majorID == 0L) {
            return lectureRepository.findByNameStartsWith(searchName).map{LectureDAO(it)}
        }
        return lectureRepository.findByMajorIDAndNameStartsWith(majorID, searchName).map{LectureDAO(it)}
    }

    override fun createNewLecture(majorID: Long, lecture:LectureDAO) : LectureDAO {
        return LectureDAO(lectureRepository.save(lecture.toLecture()))
    }

    override fun deleteLecture(lectureID:Long) : Boolean {
        val lecture : Lecture? = lectureRepository.findById(lectureID).unwrap()
        if (lecture == null) {
            return false
        } else {
            val lectureMajor = lectureRepository.getById(lectureID).majorID
            lectureRepository.deleteById(lectureID)
            if (userRepository.findBymajorID(lectureMajor).isEmpty()){
                deleteMajor(lectureMajor)
            }
            return true
        }
    }

    override fun deleteLecture(majorID: Long, lectureID:Long) : Boolean {
        val lecture : Lecture? = lectureRepository.findById(lectureID).unwrap()
        if (lecture == null) {
            return false
        } else if (lecture.majorID != majorID) {
            return false
        } else {
            deleteLecture(lectureID)
            return true
        }
    }

    /**
     * Convert Optional Datatype to a Nullable Datatype
     *
     * @param T
     * @return
     */
    fun <T> Optional<T>.unwrap(): T? = orElse(null)
}