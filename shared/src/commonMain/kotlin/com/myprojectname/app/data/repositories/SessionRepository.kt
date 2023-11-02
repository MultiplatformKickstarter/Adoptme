package com.myprojectname.app.data.repositories

import com.russhwolf.settings.Settings

const val USER_ID_KEY = "LOGGED_IN_USER_ID_KEY"
const val USER_EMAIL_KEY = "LOGGED_IN_USER_EMAIL_KEY"
const val USER_SESSION_KEY = "USER_SESSION_KEY"
const val USER_TOKEN_KEY = "USER_TOKEN_KEY"
const val IS_LOGGED_IN_KEY = "IS_LOGGED_IN_KEY"

class SessionRepository(private val settings: Settings) {

    fun getUserId(): Int {
        return settings.getInt(USER_ID_KEY, -1)
    }

    fun getEmail(): String {
        return settings.getString(USER_EMAIL_KEY, "")
    }

    fun getSession(): String {
        return settings.getString(USER_SESSION_KEY, "")
    }

    fun getToken(): String {
        return settings.getString(USER_TOKEN_KEY, "")
    }

    fun isLoggedIn(): Boolean {
        return settings.getBoolean(IS_LOGGED_IN_KEY, false)
    }

    fun initSession(id: Int, email: String, session: String, token: String) {
        settings.putInt(USER_ID_KEY, id)
        settings.putString(USER_EMAIL_KEY, email)
        settings.putString(USER_SESSION_KEY, session)
        settings.putString(USER_TOKEN_KEY, token)
        settings.putBoolean(IS_LOGGED_IN_KEY, true)
    }

    fun clear() = settings.clear()
}
