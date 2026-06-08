package com.multiplatformkickstarter.repository.chat

import com.multiplatformkickstarter.repository.user.Users
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

private const val PET_NAME_MAX_LENGTH = 128

object Conversations : Table() {
    val id: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val petId: Column<Int> = integer("petId")
    val petName: Column<String> = varchar("petName", PET_NAME_MAX_LENGTH)
    val buyerUserId: Column<Int> = integer("buyerUserId").references(Users.userId)
    val sellerUserId: Column<Int> = integer("sellerUserId").references(Users.userId)
    val createdAt: Column<Long> = long("createdAt")

    override val primaryKey = PrimaryKey(id)
}
