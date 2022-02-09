package ovh.studywithme.server.repository

import ovh.studywithme.server.model.StudyGroup
import ovh.studywithme.server.model.StudyGroupMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository : JpaRepository<StudyGroup, Long> {

    fun findByName(name:String): List<StudyGroup>

    fun findByLectureID(lectureId:Long): List<StudyGroup>
}

@Repository
interface GroupMemberRepository : JpaRepository<StudyGroupMember, Long> {

    fun findByGroupID(groupID:Long): List<StudyGroupMember>

    fun findByGroupIDAndUserID(groupID:Long, userID:Long): StudyGroupMember

    fun deleteByGroupIDAndUserID(groupID:Long, userID:Long)
}