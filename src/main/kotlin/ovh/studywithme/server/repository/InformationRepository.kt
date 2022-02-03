package ovh.studywithme.server.repository

import ovh.studywithme.server.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InformationRepository : JpaRepository<User, Long>