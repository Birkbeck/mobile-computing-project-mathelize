package com.example.culinarycompanion.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.culinarycompanion.CulinaryCompanionApp
import com.example.culinarycompanion.db.AppDatabase
import com.example.culinarycompanion.db.Category
import com.example.culinarycompanion.db.Recipe
import com.example.culinarycompanion.db.RecipeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * JVM test for our filter logic in the ViewModel.
 * We tell Robolectric to spin up our CulinaryCompanionApp class
 * so (app as CulinaryCompanionApp).db works without a ClassCastException.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(application = CulinaryCompanionApp::class, manifest = Config.NONE)
class RecipeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()
    private lateinit var vm: RecipeViewModel
    private lateinit var dao: RecipeDao

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        // Use Robolectric’s ApplicationProvider to give us an instance
        // of our custom Application subclass:
        val app = ApplicationProvider
            .getApplicationContext<CulinaryCompanionApp>()

        val db = Room.inMemoryDatabaseBuilder(app, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.recipeDao()

        // Create the VM and override its private repo field:
        vm = RecipeViewModel(app).also { realVm ->
            val repoField = RecipeViewModel::class.java
                .getDeclaredField("repo")
            repoField.isAccessible = true
            repoField.set(realVm, RecipeRepository(dao))
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun filter_switches_emission() = runTest {
        // seed two recipes
        dao.insert(
            Recipe(
                title = "A",
                ingredients = "-",
                instructions = "-",
                category = Category.Lunch
            )
        )
        dao.insert(
            Recipe(
                title = "B",
                ingredients = "-",
                instructions = "-",
                category = Category.Desserts
            )
        )


        val recorded = mutableListOf<List<Recipe>>()
        val obs = Observer<List<Recipe>> { recorded.add(it) }
        vm.recipes.observeForever(obs)

        dispatcher.scheduler.advanceUntilIdle()

        vm.setFilter(Category.Desserts)
        dispatcher.scheduler.advanceUntilIdle()

        // We should have seen exactly two emissions:
        //  1) initial all-recipes, 2) after applying the Desserts filter
        Assert.assertEquals(2, recorded.size)
        Assert.assertEquals("B", recorded.last().single().title)

        vm.recipes.removeObserver(obs)
    }
}
