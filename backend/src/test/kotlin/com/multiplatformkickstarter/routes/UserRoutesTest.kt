package com.multiplatformkickstarter.routes

import com.multiplatformkickstarter.FakeUserRepository
import com.multiplatformkickstarter.setupTestApp
import com.multiplatformkickstarter.testUser
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserRoutesTest {

    @Test
    fun `create user returns 201 and jwt token`() = testApplication {
        setupTestApp(userRepository = FakeUserRepository(emptyList()))
        val response = client.submitForm(
            url = USER_CREATE,
            formParameters = Parameters.build {
                append("email", "new@test.com")
                append("name", "New User")
                append("password", "secret")
            },
        )
        assertEquals(HttpStatusCode.Created, response.status)
        val token = response.bodyAsText()
        assertTrue(token.isNotBlank(), "Expected JWT token in response body")
    }

    @Test
    fun `create user with duplicate email returns bad request`() = testApplication {
        setupTestApp()
        val response = client.submitForm(
            url = USER_CREATE,
            formParameters = Parameters.build {
                append("email", testUser.email)
                append("name", "Duplicate")
                append("password", "secret")
            },
        )
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `create user with missing fields returns unauthorized`() = testApplication {
        setupTestApp()
        val response = client.submitForm(
            url = USER_CREATE,
            formParameters = Parameters.build {
                append("email", "missing@test.com")
                // name and password omitted
            },
        )
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `login with correct credentials returns token and user_id header`() = testApplication {
        setupTestApp()
        val response = client.submitForm(
            url = USER_LOGIN,
            formParameters = Parameters.build {
                append("email", testUser.email)
                append("password", testUser.passwordHash) // identity hash in tests
            },
        )
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(response.headers["user_id"], "Expected user_id header")
        assertTrue(response.bodyAsText().isNotBlank())
    }

    @Test
    fun `login with wrong password returns bad request`() = testApplication {
        setupTestApp()
        val response = client.submitForm(
            url = USER_LOGIN,
            formParameters = Parameters.build {
                append("email", testUser.email)
                append("password", "wrongpassword")
            },
        )
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `login with missing fields returns unauthorized`() = testApplication {
        setupTestApp()
        val response = client.submitForm(
            url = USER_LOGIN,
            formParameters = Parameters.build {
                append("email", testUser.email)
                // password omitted
            },
        )
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `logout returns 200`() = testApplication {
        setupTestApp()
        val response = client.post(USER_LOGOUT)
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
