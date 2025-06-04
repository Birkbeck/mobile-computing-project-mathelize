package com.example.culinarycompanion.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY title")
    fun all(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE category = :cat ORDER BY title")
    fun byCategory(cat: Category): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(r: Recipe): Long

    @Update suspend fun update(r: Recipe)
    @Delete suspend fun delete(r: Recipe)
}
