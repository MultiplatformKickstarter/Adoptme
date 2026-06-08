package com.multiplatformkickstarter.repository.chat

import com.multiplatformkickstarter.repository.user.Users
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

object Messages : Table() {
    val id: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val conversationId: Column<Int> = integer("conversationId").references(Conversations.id)
    val senderUserId: Column<Int> = integer("senderUserId").references(Users.userId)
    val content: Column<String> = text("content")
    val sentAt: Column<Long> = long("sentAt")

    override val primaryKey = PrimaryKey(id)
}
