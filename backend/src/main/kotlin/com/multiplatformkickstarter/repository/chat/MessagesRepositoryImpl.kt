package com.multiplatformkickstarter.repository.chat

import com.multiplatformkickstarter.models.MessageResponse
import com.multiplatformkickstarter.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.statements.InsertStatement
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select

class MessagesRepositoryImpl : MessagesRepository {

    override suspend fun sendMessage(conversationId: Int, senderUserId: Int, content: String): MessageResponse? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = Messages.insert {
                it[Messages.conversationId] = conversationId
                it[Messages.senderUserId] = senderUserId
                it[Messages.content] = content
                it[Messages.sentAt] = System.currentTimeMillis()
            }
        }
        return rowToMessage(statement?.resultedValues?.get(0))
    }

    override suspend fun getMessages(conversationId: Int): List<MessageResponse> {
        return dbQuery {
            Messages.select(
                Messages.id,
                Messages.conversationId,
                Messages.senderUserId,
                Messages.content,
                Messages.sentAt,
            ).where { Messages.conversationId eq conversationId }
                .map { rowToMessage(it)!! }
        }
    }

    private fun rowToMessage(row: ResultRow?): MessageResponse? {
        if (row == null) return null
        return MessageResponse(
            id = row[Messages.id],
            conversationId = row[Messages.conversationId],
            senderUserId = row[Messages.senderUserId],
            content = row[Messages.content],
            sentAt = row[Messages.sentAt],
        )
    }
}
