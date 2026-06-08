package com.multiplatformkickstarter.repository.favorites

import com.multiplatformkickstarter.repository.user.Users
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

object Favorites : Table() {
    val id: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val userId: Column<Int> = integer("userId").references(Users.userId)
    val petId: Column<Int> = integer("petId")
}
