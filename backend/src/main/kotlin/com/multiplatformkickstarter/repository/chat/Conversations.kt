package com.multiplatformkickstarter.repository.chat

import com.multiplatformkickstarter.repository.user.Users
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

object Conversations : Table() {
    val id: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val petId: Column<Int> = integer("petId")
    val petName: Column<String> = varchar("petName", 128)
    val buyerUserId: Column<Int> = integer("buyerUserId").references(Users.userId)
    val sellerUserId: Column<Int> = integer("sellerUserId").references(Users.userId)
    val createdAt: Column<Long> = long("createdAt")

    override val primaryKey = PrimaryKey(id)
}
