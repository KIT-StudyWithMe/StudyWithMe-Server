package ovh.studywithme.server.controller

import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.Lecture
import ovh.studywithme.server.model.Major

interface InformationControllerInterface {

    fun getAllInstitutions():List<Institution>

    fun getInstitutionByID(institutionID:Long): Institution?

    fun getInstitutionsByName(searchName:String):List<Institution>

    fun createNewInstitution(institution: Institution) : Institution

    fun deleteInstitution(institutionID:Long) : Boolean

    fun getAllMajors():List<Major>

    fun getMajorByID(majorID:Long): Major?

    fun getMajorsByName(searchName:String):List<Major>

    fun createNewMajor(major: Major) : Major

    fun deleteMajor(majorID:Long) : Boolean

    fun getAllLectures(majorID: Long):List<Lecture>

    fun getLectureByID(majorID: Long, lectureID:Long): Lecture?

    fun getLecturesByName(majorID: Long, searchName:String):List<Lecture>

    fun createNewLecture(majorID: Long, lecture: Lecture) : Lecture

    fun deleteLecture(majorID: Long, lectureID:Long) : Boolean
}