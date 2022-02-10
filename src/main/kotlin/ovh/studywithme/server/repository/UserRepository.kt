package ovh.studywithme.server.repository

import ovh.studywithme.server.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * User repository
 *
 * @constructor Create empty User repository
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {

    /**
     * Find byfirebase u i d
     *
     * @param firebaseUID
     * @return
     */
    fun findByfirebaseUID(firebaseUID : String) : List<User>
} //TODO check that firebase uid is unique