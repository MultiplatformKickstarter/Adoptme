@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.multiplatformkickstarter

import com.multiplatformkickstarter.auth.JWT_CONFIGURATION
import com.multiplatformkickstarter.auth.JwtService
import com.multiplatformkickstarter.auth.hash
import com.multiplatformkickstarter.repository.DatabaseFactory
import com.multiplatformkickstarter.repository.pets.PetsRepositoryImp
import com.multiplatformkickstarter.repository.profile.ProfileRepositoryImpl
import com.multiplatformkickstarter.repository.user.UserRepositoryImp
import com.multiplatformkickstarter.routes.pets
import com.multiplatformkickstarter.routes.profiles
import com.multiplatformkickstarter.routes.users
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.routing.routing

fun Application.configureAuthentication() {
    DatabaseFactory.init()
    val userRepository = UserRepositoryImp()
    val petRepository = PetsRepositoryImp()
    val profileRepository = ProfileRepositoryImpl()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }

    install(Authentication) {
        jwt(JWT_CONFIGURATION) {
            verifier(jwtService.verifier)
            realm = "MyProjectName Server"
            validate {
                val payload = it.payload
                val claim = payload.getClaim("id")
                val claimString = claim.asInt()
                val user = userRepository.findUser(claimString)
                user
            }
        }
    }

    routing {
        users(userRepository, jwtService, hashFunction)
        pets(petRepository, userRepository)
        profiles(profileRepository, userRepository)
    }
}
