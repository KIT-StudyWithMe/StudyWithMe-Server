package ovh.studywithme.server.dao

import ovh.studywithme.server.model.User

data class UserLight(val userID: Long, val name: String) {
    constructor(user : User) : this(user.userID, user.name)
}