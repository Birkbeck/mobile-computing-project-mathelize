package com.example.culinarycompanion.data

import com.example.culinarycompanion.db.*
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val dao: RecipeDao) {
    val all: Flow<List<Recipe>> = dao.all()
    fun byCategory(c: Category): Flow<List<Recipe>> = dao.byCategory(c)

    suspend fun save(r: Recipe) =
        if (r.id == 0L) dao.insert(r) else dao.update(r)

    suspend fun delete(r: Recipe) = dao.delete(r)
}
