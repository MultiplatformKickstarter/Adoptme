@file:OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)

package com.myprojectname.app.feature.search.repositories

import com.myprojectname.app.common.model.SearchList
import com.myprojectname.app.common.model.SearchModel
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.decodeValue
import com.russhwolf.settings.serialization.encodeValue
import kotlinx.serialization.ExperimentalSerializationApi

const val LAST_SEARCHES_KEY = "LAST_SEARCH_KEY"
const val MAXIMUM_SAVED_SEARCHES = 10

class LastSearchesRepository(
    private val settings: Settings
) {
    fun add(searchModel: SearchModel) {
        var lastSearches = settings.decodeValue(SearchList.serializer(), LAST_SEARCHES_KEY, SearchList(emptyList()))

        lastSearches = if (lastSearches.list.size < MAXIMUM_SAVED_SEARCHES) {
            lastSearches.copy(list = lastSearches.list.plus(searchModel))
        } else {
            lastSearches.copy(list = lastSearches.list.subList(1, MAXIMUM_SAVED_SEARCHES - 1).plus(searchModel))
        }

        settings.encodeValue(SearchList.serializer(), LAST_SEARCHES_KEY, lastSearches)
    }

    fun get(): SearchList {
        return settings.decodeValue(SearchList.serializer(), LAST_SEARCHES_KEY, SearchList(emptyList()))
    }
}
