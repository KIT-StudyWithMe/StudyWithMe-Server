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
interface InstitutionRepository : JpaRepository<Institution, Long>

/**
 * This major repository is used to access the study course data in the database in a generic way.
 * Functions are inherited from the JpaRepository.
 *
 * @constructor Create a major repository.
 */
@Repository
interface MajorRepository : JpaRepository<Major, Long>

/**
 * This lecture repository is used to access the lecture data in the database in a generic way.
 * Functions are inherited from the JpaRepository.
 *
 * @constructor Create a lecture repository.
 */
@Repository
interface LectureRepository : JpaRepository<Lecture, Long>