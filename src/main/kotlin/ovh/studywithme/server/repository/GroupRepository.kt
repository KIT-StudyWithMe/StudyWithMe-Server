package ovh.studywithme.server.repository

import ovh.studywithme.server.model.StudyGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository : JpaRepository<StudyGroup, Long>