package com.myprojectname.app.common.model

import kotlinx.serialization.Serializable

@Serializable
class PetModel(
    val id: Int,
    val title: String,
    val description: String,
    val images: List<String>,
    val category: PetCategory,
    val location: GeoLocation,
    val published: String,
    val modified: String?,
    val breed: String,
    val age: PetAge,
    val gender: PetGender,
    val size: PetSize,
    val color: String,
    val status: PetStatus,
    val shelterId: Int?,
    val userId: Int
)

enum class PetAge {
    BABY, YOUNG, ADULT, SENIOR;

    companion object
}

enum class PetGender {
    MALE, FEMALE;

    companion object
}

enum class PetSize {
    SMALL, MEDIUM, LARGE, XLARGE;

    companion object
}

enum class PetStatus {
    ADOPTABLE, FOUND;

    companion object
}

data class PetResponse(val id: Int)
