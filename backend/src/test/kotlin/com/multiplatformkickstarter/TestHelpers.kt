package com.multiplatformkickstarter

import com.multiplatformkickstarter.app.common.model.GeoLocation
import com.multiplatformkickstarter.app.common.model.PetAge
import com.multiplatformkickstarter.app.common.model.PetCategory
import com.multiplatformkickstarter.app.common.model.PetGender
import com.multiplatformkickstarter.app.common.model.PetModel
import com.multiplatformkickstarter.app.common.model.PetSize
import com.multiplatformkickstarter.app.common.model.PetStatus
import com.multiplatformkickstarter.auth.JWT_CONFIGURATION
import com.multiplatformkickstarter.auth.JwtService
import com.multiplatformkickstarter.auth.UserSession
import com.multiplatformkickstarter.models.ConversationResponse
import com.multiplatformkickstarter.models.DatabaseUser
import com.multiplatformkickstarter.models.MessageResponse
import com.multiplatformkickstarter.repository.chat.ConversationsRepository
import com.multiplatformkickstarter.repository.chat.MessagesRepository
import com.multiplatformkickstarter.repository.favorites.FavoritesRepository
import com.multiplatformkickstarter.repository.pets.PetsRepository
import com.multiplatformkickstarter.repository.user.UserRepository
import com.multiplatformkickstarter.plugins.configureSerialization
import com.multiplatformkickstarter.routes.chat
import com.multiplatformkickstarter.routes.favorites
import com.multiplatformkickstarter.routes.pets
import com.multiplatformkickstarter.routes.users
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.resources.Resources
import io.ktor.server.routing.routing
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.directorySessionStorage
import io.ktor.server.sessions.header
import io.ktor.server.testing.ApplicationTestBuilder
import java.io.File

// ── Shared test fixtures ──────────────────────────────────────────────────────

val testUser = DatabaseUser(userId = 1, email = "test@test.com", name = "Test User", passwordHash = "testhash")

fun makePetModel(id: Int = 1, userId: Int = 1, title: String = "Buddy") = PetModel(
    id = id, userId = userId, title = title, description = "A lovely pet",
    images = listOf("http://example.com/img.jpg"), category = PetCategory.DOGS,
    location = GeoLocation(41.4, 2.1), published = "2024-01-01T10:00",
    modified = null, breed = "Labrador", age = PetAge.YOUNG, gender = PetGender.MALE,
    size = PetSize.MEDIUM, color = "Brown", status = PetStatus.ADOPTABLE, shelterId = null,
)

// ── Fake repositories ─────────────────────────────────────────────────────────

class FakeUserRepository(seed: List<DatabaseUser> = listOf(testUser)) : UserRepository {
    private val users = seed.toMutableList()

    override suspend fun addUser(email: String, name: String, passwordHash: String): DatabaseUser? {
        if (users.any { it.email == email }) return null
        val user = DatabaseUser(userId = users.size + 1, email = email, name = name, passwordHash = passwordHash)
        users.add(user)
        return user
    }

    override suspend fun findUser(userId: Int): DatabaseUser? = users.find { it.userId == userId }

    override suspend fun findUserByEmail(email: String): DatabaseUser? = users.find { it.email == email }
}

class FakePetsRepository : PetsRepository {
    private val pets = mutableListOf<PetModel>()
    private var nextId = 1

    override suspend fun addPet(
        userId: Int, title: String, description: String, images: String,
        category: Int, location: String, published: String, breed: String,
        age: String, gender: String, size: String, color: String, status: String, shelterId: Int?,
    ): PetModel {
        val pet = makePetModel(id = nextId++, userId = userId, title = title)
        pets.add(pet)
        return pet
    }

    override suspend fun getPet(petId: Int): PetModel = pets.first { it.id == petId }

    override suspend fun getPets(userId: Int): List<PetModel> = pets.filter { it.userId == userId }

    override suspend fun delete(petId: Int) { pets.removeIf { it.id == petId } }

    override suspend fun updatePet(
        petId: Int, title: String?, description: String?, images: String?,
        location: String?, modified: String?, breed: String?, age: String?,
        gender: String?, size: String?, color: String?, status: String?, shelterId: Int?,
    ): PetModel? {
        val idx = pets.indexOfFirst { it.id == petId }.takeIf { it >= 0 } ?: return null
        val old = pets[idx]
        val updated = PetModel(
            id = old.id, userId = old.userId, title = title ?: old.title,
            description = description ?: old.description, images = old.images,
            category = old.category, location = old.location, published = old.published,
            modified = modified ?: old.modified, breed = breed ?: old.breed,
            age = if (age != null) PetAge.valueOf(age) else old.age,
            gender = if (gender != null) PetGender.valueOf(gender) else old.gender,
            size = if (size != null) PetSize.valueOf(size) else old.size,
            color = color ?: old.color, status = if (status != null) PetStatus.valueOf(status) else old.status,
            shelterId = shelterId ?: old.shelterId,
        )
        pets[idx] = updated
        return updated
    }
}

class FakeFavoritesRepository : FavoritesRepository {
    private val favorites = mutableSetOf<Pair<Int, Int>>()

    override suspend fun addFavorite(userId: Int, petId: Int): Boolean = favorites.add(userId to petId)

    override suspend fun removeFavorite(userId: Int, petId: Int): Boolean = favorites.remove(userId to petId)

    override suspend fun getFavorites(userId: Int): List<Int> =
        favorites.filter { it.first == userId }.map { it.second }

    override suspend fun isFavorite(userId: Int, petId: Int): Boolean = (userId to petId) in favorites
}

class FakeConversationsRepository : ConversationsRepository {
    val conversations = mutableListOf<ConversationResponse>()
    private var nextId = 1

    override suspend fun createConversation(
        petId: Int, petName: String, buyerUserId: Int, sellerUserId: Int,
    ): ConversationResponse {
        val conv = ConversationResponse(nextId++, petId, petName, buyerUserId, sellerUserId, 0L)
        conversations.add(conv)
        return conv
    }

    override suspend fun getConversationsForUser(userId: Int): List<ConversationResponse> =
        conversations.filter { it.buyerUserId == userId || it.sellerUserId == userId }

    override suspend fun getConversation(conversationId: Int): ConversationResponse? =
        conversations.find { it.id == conversationId }

    override suspend fun deleteConversation(conversationId: Int, userId: Int): Boolean =
        conversations.removeIf { it.id == conversationId }
}

class FakeMessagesRepository : MessagesRepository {
    val messages = mutableListOf<MessageResponse>()
    private var nextId = 1

    override suspend fun sendMessage(conversationId: Int, senderUserId: Int, content: String): MessageResponse {
        val msg = MessageResponse(nextId++, conversationId, senderUserId, content, 0L)
        messages.add(msg)
        return msg
    }

    override suspend fun getMessages(conversationId: Int): List<MessageResponse> =
        messages.filter { it.conversationId == conversationId }
}

// ── Application builder helper ────────────────────────────────────────────────

fun ApplicationTestBuilder.setupTestApp(
    userRepository: UserRepository = FakeUserRepository(),
    petsRepository: PetsRepository = FakePetsRepository(),
    favoritesRepository: FavoritesRepository = FakeFavoritesRepository(),
    conversationsRepository: ConversationsRepository = FakeConversationsRepository(),
    messagesRepository: MessagesRepository = FakeMessagesRepository(),
) {
    val jwtService = JwtService()
    val hashFunction = { s: String -> s }

    application {
        install(Resources)
        install(Sessions) {
            header<UserSession>("user_session", directorySessionStorage(File("build/.sessions-test")))
        }
        install(Authentication) {
            jwt(JWT_CONFIGURATION) {
                verifier(jwtService.verifier)
                realm = "Test"
                validate {
                    val id = it.payload.getClaim("id").asInt()
                    userRepository.findUser(id)
                }
            }
        }
        configureSerialization()
        routing {
            users(userRepository, jwtService, hashFunction)
            pets(petsRepository, userRepository)
            favorites(favoritesRepository, userRepository)
            chat(conversationsRepository, messagesRepository, userRepository)
        }
    }
}

fun JwtService.tokenForUser(user: DatabaseUser = testUser): String = generateToken(user)
