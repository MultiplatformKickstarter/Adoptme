package com.myprojectname.repository.pets

import com.myprojectname.app.common.model.GeoLocation
import com.myprojectname.app.common.model.PetAge
import com.myprojectname.app.common.model.PetCategory
import com.myprojectname.app.common.model.PetGender
import com.myprojectname.app.common.model.PetModel
import com.myprojectname.app.common.model.PetSize
import com.myprojectname.app.common.model.PetStatus
import com.myprojectname.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.update

class PetsRepositoryImp : PetsRepository {
    override suspend fun addPet(
        userId: Int,
        title: String,
        description: String,
        images: String,
        category: Int,
        location: String,
        published: String,
        breed: String,
        age: String,
        gender: String,
        size: String,
        color: String,
        status: String,
        shelterId: Int?
    ): PetModel? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = Pets.insert {
                it[Pets.userId] = userId
                it[Pets.title] = title
                it[Pets.description] = description
                it[Pets.images] = images
                it[Pets.category] = category
                it[Pets.location] = location
                it[Pets.published] = published
                it[Pets.breed] = breed
                it[Pets.age] = age
                it[Pets.gender] = gender
                it[Pets.size] = size
                it[Pets.color] = color
                it[Pets.status] = status
                it[Pets.shelterId] = shelterId ?: 0
            }
        }
        return rowToPetModel(statement?.resultedValues?.get(0))
    }

    override suspend fun getPets(userId: Int): List<PetModel> {
        return dbQuery {
            Pets.select {
                Pets.userId.eq((userId)) // 3
            }.mapNotNull { rowToPetModel(it) }
        }
    }

    override suspend fun getPet(petId: Int): PetModel {
        return dbQuery {
            Pets.select {
                Pets.id.eq((petId))
            }.mapNotNull { rowToPetModel(it) }
        }.first()
    }

    override suspend fun delete(petId: Int) {
        return dbQuery {
            Pets.deleteWhere {
                Pets.id.eq((petId))
            }
        }
    }

    override suspend fun updatePet(
        petId: Int,
        title: String?,
        description: String?,
        images: String?,
        location: String?,
        modified: String?,
        breed: String?,
        age: String?,
        gender: String?,
        size: String?,
        color: String?,
        status: String?,
        shelterId: Int?
    ): PetModel? {
        return dbQuery {
            Pets.select {
                Pets.id.eq((petId))
            }.forUpdate()

            Pets.update {
                id.eq(id)
                title?.let { title ->
                    it[Pets.title] = title
                }
                description?.let { description ->
                    it[Pets.description] = description
                }
                images?.let { images ->
                    it[Pets.images] = images
                }
                location?.let { location ->
                    it[Pets.location] = location
                }
                modified?.let { modified ->
                    it[Pets.modified] = modified
                }
                breed?.let { breed ->
                    it[Pets.breed] = breed
                }
                age?.let { age ->
                    it[Pets.age] = age
                }
                gender?.let { gender ->
                    it[Pets.gender] = gender
                }
                size?.let { size ->
                    it[Pets.size] = size
                }
                color?.let { color ->
                    it[Pets.color] = color
                }
                status?.let { status ->
                    it[Pets.status] = status
                }
                shelterId?.let { shelterId ->
                    it[Pets.shelterId] = shelterId
                }
            }

            Pets.select {
                Pets.id.eq((petId))
            }.mapNotNull { rowToPetModel(it) }
        }.firstOrNull()
    }

    private fun rowToPetModel(row: ResultRow?): PetModel? {
        if (row == null) {
            return null
        }
        val geoLocation = getGeoLocationObjectFrom(row[Pets.location])
        return PetModel(
            id = row[Pets.id],
            userId = row[Pets.userId],
            title = row[Pets.title],
            description = row[Pets.description],
            images = listOf(row[Pets.images]),
            category = PetCategory.entries[row[Pets.category]],
            location = geoLocation,
            published = row[Pets.published],
            modified = row[Pets.modified],
            breed = row[Pets.breed],
            age = PetAge.valueOf(row[Pets.age]),
            gender = PetGender.valueOf(row[Pets.gender]),
            size = PetSize.valueOf(row[Pets.size]),
            color = row[Pets.color].toString(),
            status = PetStatus.valueOf(row[Pets.status]),
            shelterId = row[Pets.shelterId]
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
