package ovh.studywithme.server.repository

import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.Major
import ovh.studywithme.server.model.Lecture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Institution repository
 *
 * @constructor Create empty Institution repository
 */
@Repository
interface InstitutionRepository : JpaRepository<Institution, Long>

/**
 * Major repository
 *
 * @constructor Create empty Major repository
 */
@Repository
interface MajorRepository : JpaRepository<Major, Long>

/**
 * Lecture repository
 *
 * @constructor Create empty Lecture repository
 */
@Repository
interface LectureRepository : JpaRepository<Lecture, Long>