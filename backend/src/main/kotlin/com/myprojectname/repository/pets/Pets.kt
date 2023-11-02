package com.myprojectname.repository.pets

import com.myprojectname.repository.user.Users
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

@Suppress("MagicNumber")
object Pets : Table() {
    val id: Column<Int> = integer("id").autoIncrement().uniqueIndex()
    val userId: Column<Int> = integer("userId").references(Users.userId)
    val title = varchar("title", 128)
    val description = varchar("description", 512)
    val images = varchar("images", 512)
    val category = integer("category")
    val location = varchar("location", 32)
    val published = varchar("published", 128)
    val modified = varchar("modified", 128)
    val breed = varchar("breed", 128)
    val age = varchar("age", 8)
    val gender = varchar("gender", 8)
    val size = varchar("size", 8)
    val color = varchar("color", 16)
    val status = varchar("status", 16)
    val shelterId = integer("shelterId")
}
