package com.myprojectname.app.feature.petupload.repositories

import com.myprojectname.app.common.model.GeoLocation
import com.myprojectname.app.common.model.PetAge
import com.myprojectname.app.common.model.PetCategory
import com.myprojectname.app.common.model.PetGender
import com.myprojectname.app.common.model.PetResponse
import com.myprojectname.app.common.model.PetSize
import com.myprojectname.app.common.model.PetStatus
import com.myprojectname.app.data.repositories.BadRequestException
import com.myprojectname.app.data.repositories.UnauthorizedException
import com.myprojectname.app.extensions.requestAndCatch
import com.myprojectname.app.platform.ServerEnvironment
import com.myprojectname.app.platform.ServiceClient
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters

private const val PET_UPLOAD_PATH = "v1/pets/upload"
class PetUploadPublishRepository(private val service: ServiceClient) {

    suspend fun upload(
        name: String,
        description: String,
        images: List<String>,
        category: PetCategory,
        location: GeoLocation,
        breed: String,
        color: String,
        age: PetAge,
        gender: PetGender,
        size: PetSize,
        status: PetStatus
    ): PetResponse {
        return service.httpClient.requestAndCatch(
            {
                val response = this.submitForm(
                    url = "${ServerEnvironment.PRODUCTION.url}/$PET_UPLOAD_PATH",
                    formParameters = Parameters.build {
                        append("name", name)
                        append("description", description)
                        append("images", images.toString())
                        append("category", category.name)
                        append("location", location.toString())
                        append("breed", breed)
                        append("color", color)
                        append("age", age.name)
                        append("gender", gender.name)
                        append("size", size.name)
                        append("status", status.name)
                    }
                )
                PetResponse(
                    id = response.headers["pet_id"]?.toInt()!!
                )
            },
            {
                when (response.status) {
                    HttpStatusCode.Unauthorized -> {
                        throw UnauthorizedException()
                    }
                    HttpStatusCode.BadRequest -> {
                        throw BadRequestException()
                    }
                    else -> throw this
                }
            }
        )
    }
}
