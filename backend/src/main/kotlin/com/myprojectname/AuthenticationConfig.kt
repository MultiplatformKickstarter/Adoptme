@file:OptIn(KtorExperimentalLocationsAPI::class)

package com.myprojectname

import com.myprojectname.auth.JWT_CONFIGURATION
import com.myprojectname.auth.JwtService
import com.myprojectname.auth.hash
import com.myprojectname.repository.DatabaseFactory
import com.myprojectname.repository.pets.PetsRepositoryImp
import com.myprojectname.repository.profile.ProfileRepositoryImpl
import com.myprojectname.repository.user.UserRepositoryImp
import com.myprojectname.routes.pets
import com.myprojectname.routes.profiles
import com.myprojectname.routes.users
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
