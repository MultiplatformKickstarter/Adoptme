package com.myprojectname.repository.profile

import com.myprojectname.app.common.model.GeoLocation
import com.myprojectname.app.common.model.Profile
import com.myprojectname.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.update

class ProfileRepositoryImpl : ProfileRepository {

    override suspend fun addProfile(
        userId: Int,
        name: String,
        description: String?,
        image: String?,
        location: String?,
        rating: Double?
    ): Profile? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = Profiles.insert { profiles ->
                profiles[Profiles.userId] = userId
                profiles[Profiles.name] = name
                description?.let {
                    profiles[Profiles.description] = it
                }
                image?.let {
                    profiles[Profiles.image] = it
                }
                location?.let {
                    profiles[Profiles.location] = it
                }
                rating?.let {
                    profiles[Profiles.rating] = it
                }
            }
        }
        return rowToProfiles(statement?.resultedValues?.get(0))
    }

    override suspend fun getProfile(userId: Int): Profile? {
        return dbQuery {
            Profiles.select {
                Profiles.userId.eq((userId))
            }.mapNotNull { rowToProfiles(it) }
        }.firstOrNull()
    }

    override suspend fun updateProfile(
        userId: Int,
        name: String?,
        description: String?,
        image: String?,
        location: String?,
        rating: Double?
    ): Profile? {
        return dbQuery {
            Profiles.select {
                Profiles.userId.eq((userId))
            }.forUpdate()

            Profiles.update {
                Profiles.userId.eq(userId)
                name?.let { name ->
                    it[Profiles.name] = name
                }
                description?.let { description ->
                    it[Profiles.description] = description
                }
                image?.let { image ->
                    it[Profiles.image] = image
                }
                location?.let { location ->
                    it[Profiles.location] = location
                }
                rating?.let { rating ->
                    it[Profiles.rating] = rating
                }
            }

            Profiles.select {
                Profiles.userId.eq((userId))
            }.mapNotNull { rowToProfiles(it) }
        }.firstOrNull()
    }

    private fun rowToProfiles(row: ResultRow?): Profile? {
        if (row == null) {
            return null
        }
        val geoLocation = getGeoLocationObjectFrom(row[Profiles.location])
        return Profile(
            id = row[Profiles.id],
            userId = row[Profiles.userId],
            name = row[Profiles.name],
            description = row[Profiles.description],
            image = row[Profiles.image],
            location = geoLocation,
            rating = row[Profiles.rating]
        )
    }

    private fun getGeoLocationObjectFrom(rowText: String): GeoLocation {
        val geoLocationText = rowText.split(',')
        return if (geoLocationText.size > 1) {
            GeoLocation(geoLocationText[0].toDouble(), geoLocationText[1].toDouble())
        } else {
            GeoLocation(0.0, 0.0)
        }
    }
}
