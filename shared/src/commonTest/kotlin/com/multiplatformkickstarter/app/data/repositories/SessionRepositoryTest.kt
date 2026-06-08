package com.multiplatformkickstarter.app.data.repositories

import com.russhwolf.settings.MapSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SessionRepositoryTest {

    private fun repo() = SessionRepository(MapSettings())

    @Test
    fun `new repo is not logged in`() {
        assertFalse(repo().isLoggedIn())
    }

    @Test
    fun `initSession marks user as logged in`() {
        val repo = repo()
        repo.initSession(id = 42, email = "a@b.com", session = "sess", token = "tok")
        assertTrue(repo.isLoggedIn())
    }

    @Test
    fun `initSession stores userId`() {
        val repo = repo()
        repo.initSession(id = 7, email = "a@b.com", session = "sess", token = "tok")
        assertEquals(7, repo.getUserId())
    }

    @Test
    fun `initSession stores email`() {
        val repo = repo()
        repo.initSession(id = 1, email = "user@example.com", session = "s", token = "t")
        assertEquals("user@example.com", repo.getEmail())
    }

    @Test
    fun `initSession stores token`() {
        val repo = repo()
        repo.initSession(id = 1, email = "e", session = "s", token = "my-jwt-token")
        assertEquals("my-jwt-token", repo.getToken())
    }

    @Test
    fun `clear resets login state`() {
        val repo = repo()
        repo.initSession(id = 1, email = "e", session = "s", token = "t")
        repo.clear()
        assertFalse(repo.isLoggedIn())
    }

    @Test
    fun `clear resets userId to default`() {
        val repo = repo()
        repo.initSession(id = 5, email = "e", session = "s", token = "t")
        repo.clear()
        assertEquals(-1, repo.getUserId())
    }
}
