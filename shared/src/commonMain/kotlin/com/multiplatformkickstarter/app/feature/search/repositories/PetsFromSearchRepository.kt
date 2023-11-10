package com.multiplatformkickstarter.app.feature.search.repositories

import co.touchlab.kermit.Logger
import com.multiplatformkickstarter.app.common.model.PetModel
import com.multiplatformkickstarter.app.common.model.SearchModel
import com.multiplatformkickstarter.app.extensions.requestAndCatch
import com.multiplatformkickstarter.app.platform.ServerEnvironment
import com.multiplatformkickstarter.app.platform.ServiceClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

private const val SEARCH_PATH = "v1/search"

class PetsFromSearchRepository(private val service: ServiceClient) {

    suspend fun getPets(searchModel: SearchModel): GetPetsResponse {
        Logger.d(searchModel.text.toString())
        return service.httpClient.requestAndCatch(
            {
                this.get("${ServerEnvironment.PRODUCTION.url}/$SEARCH_PATH/").body()
            },
            {
                when (response.status) {
                    // TODO: Return correct errors
                    HttpStatusCode.BadRequest -> { throw this }
                    HttpStatusCode.Conflict -> { throw this }
                    else -> throw this
                }
            }
        )
    }
}

data class GetPetsResponse(val pets: List<PetModel>)
