package com.myprojectname.repository.pets

import com.myprojectname.app.common.model.PetModel

interface PetsRepository {
    suspend fun addPet(
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
    ): PetModel?

    suspend fun getPet(petId: Int): PetModel

    suspend fun getPets(userId: Int): List<PetModel>

    suspend fun delete(petId: Int)

    suspend fun updatePet(
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
    ): PetModel?
}
