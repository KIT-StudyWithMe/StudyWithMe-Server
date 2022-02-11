package ovh.studywithme.server.controller

import ovh.studywithme.server.dao.InstitutionDAO
import ovh.studywithme.server.dao.LectureDAO
import ovh.studywithme.server.dao.MajorDAO

/**
 * Information controller interface
 */
interface InformationControllerInterface {

    /**
     * Get all institutions.
     *
     * @return A list containing all institutions.
     */
    fun getAllInstitutions():List<InstitutionDAO>

    /**
     * Get a certain institution.
     * If the institution was found, it's returned. If not, null is returned.
     *
     * @param institutionID The institution's unique identifier.
     * @return The institution that was requested.
     */
    fun getInstitutionByID(institutionID:Long): InstitutionDAO?

    /**
     * Get an institution by its name.
     *
     * @param searchName The institution's name the user is looking for.
     * @return A list of institutions whose names match the name the user is looking for.
     */
    fun getInstitutionsByName(searchName:String):List<InstitutionDAO>

    /**
     * Create a new institution.
     * The method returns the newly created institution with the generated institutionID.
     *
     * @param institution The institution information with which the institution should be created.
     * @return The institution that was created.
     */
    fun createNewInstitution(institution: InstitutionDAO) : InstitutionDAO

    /**
     * Delete an institution that is identified by its id.
     *
     * @param institutionID The institution's unique identifier.
     * @return A boolean which is true if the institution was found and then deleted and false otherwise.
     */
    fun deleteInstitution(institutionID:Long) : Boolean

    /**
     * Get all majors.
     *
     * @return A list containing all majors.
     */
    fun getAllMajors():List<MajorDAO>

    /**
     * Get a certain major.
     * If the major was found, it's returned. If not, null is returned.
     *
     * @param majorID The major's unique identifier.
     * @return The major that was requested.
     */
    fun getMajorByID(majorID:Long): MajorDAO?

    /**
     * Get a major by its name.
     *
     * @param searchName The major's name the user is looking for.
     * @return A list of majors whose names match the name the user is looking for.
     */
    fun getMajorsByName(searchName:String):List<MajorDAO>

    /**
     * Create a new major.
     * The method returns the newly created major with the generated majorID.
     *
     * @param major The major information with which the major should be created.
     * @return The major that was created.
     */
    fun createNewMajor(major: MajorDAO) : MajorDAO

    /**
     * Delete a major that is identified by its id.
     *
     * @param majorID The major's unique identifier.
     * @return A boolean which is true if the major was found and then deleted and false otherwise.
     */
    fun deleteMajor(majorID:Long) : Boolean

    /**
     * Get all lectures.
     *
     * @return A list containing all lectures.
     */
    fun getAllLectures(majorID: Long):List<LectureDAO>

    /**
     * Get a certain lecture.
     * If the lecture was found, it's returned. If not, null is returned.
     *
     * @param lectureID The lecture's unique identifier.
     * @return The lecture that was requested.
     */
    fun getLectureByID(lectureID:Long): LectureDAO?

    /**
     * Get a lecture by its name.
     *
     * @param majorID The unique identifier of the major the lecture was created for.
     * @param searchName The lecture's name the user is looking for.
     * @return A list of lectures whose names match the name the user is looking for.
     */
    fun getLecturesByName(majorID: Long, searchName:String):List<LectureDAO>

    /**
     * Create a new lecture.
     * The method returns the newly created lecture with the generated lectureID.
     *
     * @param majorID The unique identifier of the major the lecture was created for.
     * @param lecture The lecture information with which the lecture should be created.
     * @return The newly created lecture.
     */
    fun createNewLecture(majorID: Long, lecture: LectureDAO) : LectureDAO

    /**
     * Delete a lecture that is identified by its id.
     *
     * @param majorID The unique identifier of the major the lecture was created for.
     * @param lectureID The lecture's unique identifier.
     * @return A boolean which is true if the lecture was found and then deleted and false otherwise.
     */
    fun deleteLecture(majorID: Long, lectureID:Long) : Boolean
}