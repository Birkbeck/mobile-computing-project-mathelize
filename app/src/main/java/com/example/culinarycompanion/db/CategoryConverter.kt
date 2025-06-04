package com.example.culinarycompanion.db

import androidx.room.TypeConverter

class CategoryConverter {
    @TypeConverter fun fromCategory(c: Category): String = c.name
    @TypeConverter fun toCategory(s: String): Category = Category.valueOf(s)
}
