package com.example.culinarycompanion.data

import android.app.Application
import androidx.lifecycle.*
import com.example.culinarycompanion.CulinaryCompanionApp
import com.example.culinarycompanion.db.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class RecipeViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = RecipeRepository((app as CulinaryCompanionApp).db.recipeDao())

    private val filter = MutableStateFlow<Category?>(null)

    val recipes: LiveData<List<Recipe>> = filter
        .flatMapLatest { it?.let(repo::byCategory) ?: repo.all }
        .asLiveData()

    fun setFilter(c: Category?) { filter.value = c }
    fun save(r: Recipe) = viewModelScope.launch { repo.save(r) }
    fun delete(r: Recipe) = viewModelScope.launch { repo.delete(r) }
}
