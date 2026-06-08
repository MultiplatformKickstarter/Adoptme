package com.multiplatformkickstarter.routes

import com.multiplatformkickstarter.FakeFavoritesRepository
import com.multiplatformkickstarter.FakeUserRepository
import com.multiplatformkickstarter.auth.JwtService
import com.multiplatformkickstarter.setupTestApp
import com.multiplatformkickstarter.testUser
import com.multiplatformkickstarter.tokenForUser
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class FavoritesRoutesTest {

    private val jwtService = JwtService()
    private val token get() = jwtService.tokenForUser()

    @Test
    fun `get favorites without auth returns 401`() = testApplication {
        setupTestApp()
        val response = client.get(FAVORITES)
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `get favorites returns empty list for new user`() = testApplication {
        setupTestApp()
        val response = client.get(FAVORITES) {
            header("Authorization", "Bearer $token")
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `add favorite returns 201`() = testApplication {
        setupTestApp()
        val response = client.submitForm(
            url = FAVORITES_ADD,
            formParameters = Parameters.build { append("petId", "42") },
        ) {
            header("Authorization", "Bearer $token")
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `add same favorite twice returns conflict`() = testApplication {
        val favRepo = FakeFavoritesRepository()
        setupTestApp(favoritesRepository = favRepo)
        val params = Parameters.build { append("petId", "42") }
        client.submitForm(url = FAVORITES_ADD, formParameters = params) {
            header("Authorization", "Bearer $token")
        }
        val second = client.submitForm(url = FAVORITES_ADD, formParameters = params) {
            header("Authorization", "Bearer $token")
        }
        assertEquals(HttpStatusCode.Conflict, second.status)
    }

    @Test
    fun `add favorite with missing petId returns bad request`() = testApplication {
        setupTestApp()
        val response = client.submitForm(
            url = FAVORITES_ADD,
            formParameters = Parameters.build {},
        ) {
            header("Authorization", "Bearer $token")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `remove existing favorite returns 200`() = testApplication {
        val favRepo = FakeFavoritesRepository()
        setupTestApp(favoritesRepository = favRepo)
        favRepo.addFavorite(testUser.userId, 42)
        val response = client.delete(FAVORITES_REMOVE) {
            header("Authorization", "Bearer $token")
            url { parameters.append("petId", "42") }
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `remove non-existing favorite returns 404`() = testApplication {
        setupTestApp()
        val response = client.delete(FAVORITES_REMOVE) {
            header("Authorization", "Bearer $token")
            url { parameters.append("petId", "99") }
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}
