package ovh.studywithme.server.controller

import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.Lecture
import ovh.studywithme.server.model.Major

/**
 * Information controller interface
 *
 * @constructor Create empty Information controller interface
 */
interface InformationControllerInterface {

    /**
     * Get all institutions
     *
     * @return
     */
    fun getAllInstitutions():List<Institution>

    /**
     * Get institution by i d
     *
     * @param institutionID
     * @return
     */
    fun getInstitutionByID(institutionID:Long): Institution?

    /**
     * Get institutions by name
     *
     * @param searchName
     * @return
     */
    fun getInstitutionsByName(searchName:String):List<Institution>

    /**
     * Create new institution
     *
     * @param institution
     * @return
     */
    fun createNewInstitution(institution: Institution) : Institution

    /**
     * Delete institution
     *
     * @param institutionID
     * @return
     */
    fun deleteInstitution(institutionID:Long) : Boolean

    /**
     * Get all majors
     *
     * @return
     */
    fun getAllMajors():List<Major>

    /**
     * Get major by i d
     *
     * @param majorID
     * @return
     */
    fun getMajorByID(majorID:Long): Major?

    /**
     * Get majors by name
     *
     * @param searchName
     * @return
     */
    fun getMajorsByName(searchName:String):List<Major>

    /**
     * Create new major
     *
     * @param major
     * @return
     */
    fun createNewMajor(major: Major) : Major

    /**
     * Delete major
     *
     * @param majorID
     * @return
     */
    fun deleteMajor(majorID:Long) : Boolean

    /**
     * Get all lectures
     *
     * @param majorID
     * @return
     */
    fun getAllLectures(majorID: Long):List<Lecture>

    /**
     * Get lecture by i d
     *
     * @param majorID
     * @param lectureID
     * @return
     */
    fun getLectureByID(majorID: Long, lectureID:Long): Lecture?

    /**
     * Get lectures by name
     *
     * @param majorID
     * @param searchName
     * @return
     */
    fun getLecturesByName(majorID: Long, searchName:String):List<Lecture>

    /**
     * Create new lecture
     *
     * @param majorID
     * @param lecture
     * @return
     */
    fun createNewLecture(majorID: Long, lecture: Lecture) : Lecture

    /**
     * Delete lecture
     *
     * @param majorID
     * @param lectureID
     * @return
     */
    fun deleteLecture(majorID: Long, lectureID:Long) : Boolean
}