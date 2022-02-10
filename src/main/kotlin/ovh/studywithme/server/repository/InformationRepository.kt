package ovh.studywithme.server.repository

import ovh.studywithme.server.model.Institution
import ovh.studywithme.server.model.Major
import ovh.studywithme.server.model.Lecture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InstitutionRepository : JpaRepository<Institution, Long>

@Repository
interface MajorRepository : JpaRepository<Major, Long>

@Repository
interface LectureRepository : JpaRepository<Lecture, Long>