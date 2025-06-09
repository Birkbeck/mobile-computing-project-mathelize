package com.example.culinarycompanion.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Pure‐JVM tests for our Room DAO.
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class RecipeDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: RecipeDao

    @Before fun setUp() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.recipeDao()
    }

    @After fun tearDown() {
        db.close()
    }

    @Test fun insert_and_read_back() = runTest {
        val omelette = Recipe(
            title = "Omelette",
            ingredients = "Eggs",
            instructions = "Whisk",
            category = Category.Breakfast
        )
        val id = dao.insert(omelette)
        val saved = dao.all().first().single()
        Assert.assertEquals(id, saved.id)
        Assert.assertEquals("Omelette", saved.title)
    }

    @Test fun update_changes_persist() = runTest {
        val id = dao.insert(Recipe(
            title = "Toast",
            ingredients = "-",
            instructions = "-",
            category = Category.Breakfast
        ))
        val toast = dao.all().first().single()
        dao.update(toast.copy(title = "French Toast"))
        val updated = dao.all().first().single()
        Assert.assertEquals("French Toast", updated.title)
    }

    @Test fun delete_removes_row() = runTest {
        dao.insert(Recipe(
            title = "Tea",
            ingredients = "-",
            instructions = "-",
            category = Category.Other
        ))
        dao.delete(dao.all().first().single())
        val remaining = dao.all().first()
        Assert.assertTrue(remaining.isEmpty())
    }

    @Test fun byCategory_returns_only_matching() = runTest {
        dao.insert(Recipe(
            title = "Salad",
            ingredients = "-",
            instructions = "-",
            category = Category.Lunch
        ))
        dao.insert(Recipe(
            title = "Cake",
            ingredients = "-",
            instructions = "-",
            category = Category.Desserts
        ))
        val desserts = dao.byCategory(Category.Desserts).first()
        Assert.assertEquals(1, desserts.size)
        Assert.assertEquals("Cake", desserts.first().title)
    }
}
