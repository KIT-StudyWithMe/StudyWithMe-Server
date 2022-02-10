package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.InstitutionDAO
import ovh.studywithme.server.dao.LectureDAO
import ovh.studywithme.server.dao.MajorDAO

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
    fun getAllInstitutions():List<InstitutionDAO>

    /**
     * Get institution by i d
     *
     * @param institutionID
     * @return
     */
    fun getInstitutionByID(institutionID:Long): InstitutionDAO?

    /**
     * Get institutions by name
     *
     * @param searchName
     * @return
     */
    fun getInstitutionsByName(searchName:String):List<InstitutionDAO>

    /**
     * Create new institution
     *
     * @param institution
     * @return
     */
    fun createNewInstitution(institution: InstitutionDAO) : InstitutionDAO

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
    fun getAllMajors():List<MajorDAO>

    /**
     * Get major by i d
     *
     * @param majorID
     * @return
     */
    fun getMajorByID(majorID:Long): MajorDAO?

    /**
     * Get majors by name
     *
     * @param searchName
     * @return
     */
    fun getMajorsByName(searchName:String):List<MajorDAO>

    /**
     * Create new major
     *
     * @param major
     * @return
     */
    fun createNewMajor(major: MajorDAO) : MajorDAO

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
    fun getAllLectures(majorID: Long):List<LectureDAO>

    /**
     * Get lecture by i d
     *
     * @param majorID
     * @param lectureID
     * @return
     */
    fun getLectureByID(majorID: Long, lectureID:Long): LectureDAO?

    /**
     * Get lectures by name
     *
     * @param majorID
     * @param searchName
     * @return
     */
    fun getLecturesByName(majorID: Long, searchName:String):List<LectureDAO>

    /**
     * Create new lecture
     *
     * @param majorID
     * @param lecture
     * @return
     */
    fun createNewLecture(majorID: Long, lecture: LectureDAO) : LectureDAO

    /**
     * Delete lecture
     *
     * @param majorID
     * @param lectureID
     * @return
     */
    fun deleteLecture(majorID: Long, lectureID:Long) : Boolean
}