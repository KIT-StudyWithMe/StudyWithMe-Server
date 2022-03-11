package ovh.studywithme.server.repository

import ovh.studywithme.server.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * This user repository is used to access the user data in the database in a generic way.
 * Many functions are inherited from the JpaRepository. Even the functions declared below are automatically implemented.
 *
 * @constructor Create a user repository.
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {

    /**
     * Finds and returns all users in the database that belong to the given firebaseUID.
     * This should only be a single user as the firebase uid is supposed to uniquely identify a user.
     *
     * @param firebaseUID A firebase user ID.
     * @return A list of all users that the given firebase token is stored for.
     */
    fun findByfirebaseUID(firebaseUID : String) : List<User>

    /**
     * Finds and returns all users in the database that have a specific major.
     * This call is used to determine when a major has to be deleted
     *
     * @param majorID A major ID.
     * @return A list of all users that the given firebase token is stored for.
     */
    fun findBymajorID(majorID : Long) : List<User>
} //TODO check that firebase uid is unique