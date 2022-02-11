package ovh.studywithme.server.repository

import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.Major
import ovh.studywithme.server.model.Lecture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * The following repositories are grouped under the name "information repositories", as they all contain information
 * about what and where a user studies.
 */


/**
 * This institute repository is used to access the institute data in the database in a generic way.
 * Functions are inherited from the JpaRepository.
 *
 * @constructor Create an institute repository.
 */
@Repository
interface InstitutionRepository : JpaRepository<Institution, Long> {
    /**
     * Find a list of all Institutions with a specific name.
     *
     * @param name Name of the Institutions to search for.
     * @return A list of Institutions with that name.
     */
    fun findByNameStartsWith(name : String) : List<Institution>
}

/**
 * This major repository is used to access the study course data in the database in a generic way.
 * Functions are inherited from the JpaRepository.
 *
 * @constructor Create a major repository.
 */
@Repository
interface MajorRepository : JpaRepository<Major, Long> {
    /**
     * Find a list of all Majors with a specific name.
     *
     * @param name Name of the Major to search for.
     * @return A list of Majors with that name.
     */
    fun findByNameStartsWith(name : String) : List<Major>
}

/**
 * This lecture repository is used to access the lecture data in the database in a generic way.
 * Functions are inherited from the JpaRepository.
 *
 * @constructor Create a lecture repository.
 */
@Repository
interface LectureRepository : JpaRepository<Lecture, Long> {
    /**
     * Find a list of all Lectures with a specific name and a majorID.
     * The majorID is needed because there could be a Lecture for many majors.
     *
     * @param name Name of the Lecture to search for.
     * @param majorID Major ID of the Lectures to search for
     * @return A list of Lectures with that name.
     */
    fun findByMajorIDAndNameStartsWith(majorID: Long, name : String) : List<Lecture>
}