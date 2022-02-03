package ovh.studywithme.server.repository

import ovh.studywithme.server.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByfirebaseUID(firebaseUID : String) : List<User>
} //TODO check that firebase uid is unique