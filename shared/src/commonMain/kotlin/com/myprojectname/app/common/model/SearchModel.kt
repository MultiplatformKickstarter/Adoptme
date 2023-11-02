package com.myprojectname.app.common.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchList(val list: List<SearchModel>)

@Serializable
data class SearchModel(
    val text: String? = null,
    val petCategory: PetCategory? = null,
    val location: GeoLocation? = null
)
