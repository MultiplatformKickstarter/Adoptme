package com.multiplatformkickstarter.repository.chat

import com.multiplatformkickstarter.models.ConversationResponse
import com.multiplatformkickstarter.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.core.statements.InsertStatement
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select

class ConversationsRepositoryImpl : ConversationsRepository {

    override suspend fun createConversation(
        petId: Int,
        petName: String,
        buyerUserId: Int,
        sellerUserId: Int,
    ): ConversationResponse? {
        val existing = dbQuery {
            Conversations.select(Conversations.id)
                .where { (Conversations.petId eq petId) and (Conversations.buyerUserId eq buyerUserId) }
                .map { rowToConversation(it) }
                .singleOrNull()
        }
        if (existing != null) return existing

        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = Conversations.insert {
                it[Conversations.petId] = petId
                it[Conversations.petName] = petName
                it[Conversations.buyerUserId] = buyerUserId
                it[Conversations.sellerUserId] = sellerUserId
                it[Conversations.createdAt] = System.currentTimeMillis()
            }
        }
        return rowToConversation(statement?.resultedValues?.get(0))
    }

    override suspend fun getConversationsForUser(userId: Int): List<ConversationResponse> {
        return dbQuery {
            Conversations.select(
                Conversations.id,
                Conversations.petId,
                Conversations.petName,
                Conversations.buyerUserId,
                Conversations.sellerUserId,
                Conversations.createdAt,
            ).where { (Conversations.buyerUserId eq userId) or (Conversations.sellerUserId eq userId) }
                .map { rowToConversation(it)!! }
        }
    }

    override suspend fun getConversation(conversationId: Int): ConversationResponse? {
        return dbQuery {
            Conversations.select(Conversations.id)
                .where { Conversations.id eq conversationId }
                .map { rowToConversation(it) }
                .singleOrNull()
        }
    }

    override suspend fun deleteConversation(conversationId: Int, userId: Int): Boolean {
        val deleted = dbQuery {
            Conversations.deleteWhere {
                (Conversations.id eq conversationId) and
                    ((Conversations.buyerUserId eq userId) or (Conversations.sellerUserId eq userId))
            }
        }
        return deleted > 0
    }

    private fun rowToConversation(row: ResultRow?): ConversationResponse? {
        if (row == null) return null
        return ConversationResponse(
            id = row[Conversations.id],
            petId = row[Conversations.petId],
            petName = row[Conversations.petName],
            buyerUserId = row[Conversations.buyerUserId],
            sellerUserId = row[Conversations.sellerUserId],
            createdAt = row[Conversations.createdAt],
        )
    }
}
