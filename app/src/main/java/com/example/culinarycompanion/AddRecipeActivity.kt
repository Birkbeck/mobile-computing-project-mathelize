package com.example.culinarycompanion

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.culinarycompanion.data.RecipeViewModel
import com.example.culinarycompanion.db.Category
import com.example.culinarycompanion.db.Recipe
import com.google.android.material.appbar.MaterialToolbar

class AddRecipeActivity : AppCompatActivity() {

    private val vm: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        val tb = findViewById<MaterialToolbar>(R.id.addToolbar)
        setSupportActionBar(tb)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tb.setNavigationOnClickListener { finish() }

        val spCategory = findViewById<Spinner>(R.id.spCategory)
        ArrayAdapter.createFromResource(
            this, R.array.recipe_categories,
            android.R.layout.simple_spinner_item
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spCategory.adapter = it
        }

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etIngredients = findViewById<EditText>(R.id.etIngredients)
        val etInstructions = findViewById<EditText>(R.id.etInstructions)

        // editing?
        val editId = intent.getLongExtra("id", 0L)
        if (editId != 0L) {
            vm.recipes.observe(this) { list ->
                list.firstOrNull { it.id == editId }?.let { r ->
                    tb.title = getString(R.string.title_edit_recipe)
                    spCategory.setSelection(r.category.ordinal)
                    etTitle.setText(r.title)
                    etIngredients.setText(r.ingredients)
                    etInstructions.setText(r.instructions)
                }
            }
        } else tb.title = getString(R.string.title_add_recipe)

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val recipe = Recipe(
                id = editId,
                title = etTitle.text.toString().trim(),
                ingredients = etIngredients.text.toString().trim(),
                instructions = etInstructions.text.toString().trim(),
                category = Category.values()[spCategory.selectedItemPosition]
            )
            vm.save(recipe)
            finish()
        }
    }
}
