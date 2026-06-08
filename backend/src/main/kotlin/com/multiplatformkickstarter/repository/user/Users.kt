package com.multiplatformkickstarter.repository.user

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

@Suppress("MagicNumber")
object Users : Table() {
    val userId: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val email = varchar("email", 128).uniqueIndex()
    val name = varchar("name", 256)
    val passwordHash = varchar("password_hash", 64)
}
