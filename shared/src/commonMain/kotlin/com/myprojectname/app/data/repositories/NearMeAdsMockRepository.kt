package com.myprojectname.app.data.repositories

import com.myprojectname.app.common.model.GeoLocation
import com.myprojectname.app.common.model.PetAge
import com.myprojectname.app.common.model.PetCategory
import com.myprojectname.app.common.model.PetGender
import com.myprojectname.app.common.model.PetModel
import com.myprojectname.app.common.model.PetSize
import com.myprojectname.app.common.model.PetStatus

class NearMeAdsMockRepository {
    private val itemList: List<PetModel> = listOf(
        PetModel(
            0,
            "Clove",
            "Clove is a sweet and playful cat. She loves to play with toys and explore and loves to soak up.",
            listOf("https://upload.wikimedia.org/wikipedia/commons/4/4c/Blackcat-Lilith.jpg"),
            PetCategory.CATS,
            GeoLocation(41.391568, 2.151914),
            "2023-5-12T16:35",
            "2023-5-12T16:35",
            "Domestic Short Hair",
            PetAge.ADULT,
            PetGender.FEMALE,
            PetSize.MEDIUM,
            "Black",
            PetStatus.ADOPTABLE,
            null,
            0
        ),
        PetModel(
            1,
            "River",
            "River is sweet and gentle, she loves her human and needs lots of love.",
            listOf("https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Toyger_-_Cornish_Rex_presentation_show_Riihim%C3%A4ki_2008-11-16_IMG_0101.JPG/800px-Toyger_-_Cornish_Rex_presentation_show_Riihim%C3%A4ki_2008-11-16_IMG_0101.JPG"),
            PetCategory.CATS,
            GeoLocation(41.391568, 2.151914),
            "2023-5-12T16:35",
            "2023-5-12T16:35",
            "Domestic Short Hair",
            PetAge.ADULT,
            PetGender.FEMALE,
            PetSize.MEDIUM,
            "Tiger",
            PetStatus.ADOPTABLE,
            null,
            0
        ),
        PetModel(
            2,
            "Louie",
            "Fun dog.",
            listOf("https://images.unsplash.com/photo-1560603065-d99d67c6efe5?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1yZWxhdGVkfDE0fHx8ZW58MHx8fHx8&w=1000&q=80"),
            PetCategory.DOGS,
            GeoLocation(41.391568, 2.151914),
            "2023-5-12T16:35",
            "2023-5-12T16:35",
            "Mixed Breed",
            PetAge.ADULT,
            PetGender.MALE,
            PetSize.XLARGE,
            "Black and Brown",
            PetStatus.ADOPTABLE,
            null,
            0
        ),
        PetModel(
            3,
            "Hope",
            "Come and meet Hope!",
            listOf("https://upload.wikimedia.org/wikipedia/commons/4/40/Harlis-2009-15-10.jpg"),
            PetCategory.DOGS,
            GeoLocation(41.391568, 2.151914),
            "2023-5-12T16:35",
            "2023-5-12T16:35",
            "Mixed Breed",
            PetAge.BABY,
            PetGender.MALE,
            PetSize.MEDIUM,
            "Brindle",
            PetStatus.ADOPTABLE,
            null,
            0
        ),
        PetModel(
            4,
            "Dennis",
            "Come and meet Dennis!",
            listOf("https://upload.wikimedia.org/wikipedia/commons/b/bf/Yellow_dog.jpg"),
            PetCategory.DOGS,
            GeoLocation(41.391568, 2.151914),
            "2023-5-12T16:35",
            "2023-5-12T16:35",
            "Domestic Short Hair",
            PetAge.ADULT,
            PetGender.MALE,
            PetSize.SMALL,
            "Yellow",
            PetStatus.ADOPTABLE,
            null,
            0
        ),
        PetModel(
            5,
            "Clove",
            "Clove is a sweet and playful cat. She loves to play with toys and explore and loves to soak up.",
            listOf("https://upload.wikimedia.org/wikipedia/commons/4/4c/Blackcat-Lilith.jpg"),
            PetCategory.CATS,
            GeoLocation(41.391568, 2.151914),
            "2023-5-12T16:35",
            "2023-5-12T16:35",
            "Domestic Short Hair",
            PetAge.ADULT,
            PetGender.FEMALE,
            PetSize.MEDIUM,
            "Black",
            PetStatus.ADOPTABLE,
            null,
            0
        ),
        PetModel(
            6,
            "River",
            "River is sweet and gentle, she loves her human and needs lots of love.",
            listOf("https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Toyger_-_Cornish_Rex_presentation_show_Riihim%C3%A4ki_2008-11-16_IMG_0101.JPG/800px-Toyger_-_Cornish_Rex_presentation_show_Riihim%C3%A4ki_2008-11-16_IMG_0101.JPG"),
            PetCategory.CATS,
            GeoLocation(41.391568, 2.151914),
            "2023-5-12T16:35",
            "2023-5-12T16:35",
            "Domestic Short Hair",
            PetAge.ADULT,
            PetGender.FEMALE,
            PetSize.MEDIUM,
            "Tiger",
            PetStatus.ADOPTABLE,
            null,
            0
        ),
        PetModel(
            7,
            "Louie",
            "Fun dog.",
            listOf("https://images.unsplash.com/photo-1560603065-d99d67c6efe5?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1yZWxhdGVkfDE0fHx8ZW58MHx8fHx8&w=1000&q=80"),
            PetCategory.DOGS,
            GeoLocation(41.391568, 2.151914),
            "2023-5-12T16:35",
            "2023-5-12T16:35",
            "Mixed Breed",
            PetAge.ADULT,
            PetGender.MALE,
            PetSize.XLARGE,
            "Black and Brown",
            PetStatus.ADOPTABLE,
            null,
            0
        ),
        PetModel(
            8,
            "Hope",
            "Come and meet Hope!",
            listOf("https://upload.wikimedia.org/wikipedia/commons/4/40/Harlis-2009-15-10.jpg"),
            PetCategory.DOGS,
            GeoLocation(41.391568, 2.151914),
            "2023-5-12T16:35",
            "2023-5-12T16:35",
            "Mixed Breed",
            PetAge.BABY,
            PetGender.MALE,
            PetSize.MEDIUM,
            "Brindle",
            PetStatus.ADOPTABLE,
            null,
            0
        ),
        PetModel(
            9,
            "Dennis",
            "Come and meet Dennis!",
            listOf("https://upload.wikimedia.org/wikipedia/commons/b/bf/Yellow_dog.jpg"),
            PetCategory.DOGS,
            GeoLocation(41.391568, 2.151914),
            "2023-5-12T16:35",
            "2023-5-12T16:35",
            "Domestic Short Hair",
            PetAge.ADULT,
            PetGender.MALE,
            PetSize.SMALL,
            "Yellow",
            PetStatus.ADOPTABLE,
            null,
            0
        )
    )

    fun getAds(): Result<List<PetModel>> = runCatching {
        return@runCatching itemList
    }

    fun getFeaturedAd(id: Int): PetModel? {
        return itemList.find { id == it.id }
    }
}
