package com.example.culinarycompanion.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val ingredients: String,
    val instructions: String,
    val category: Category
)

enum class Category { Breakfast, Brunch, Lunch, Dinner, Desserts, Other }
