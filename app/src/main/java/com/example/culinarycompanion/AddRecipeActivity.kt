package com.example.culinarycompanion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

// Add Recipe File
class AddRecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        // toolbar
        val tb = findViewById<MaterialToolbar>(R.id.addToolbar)
        setSupportActionBar(tb)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tb.setNavigationOnClickListener { finish() }
        tb.title = getString(R.string.title_add_recipe)

        // category spinner
        findViewById<Spinner>(R.id.spCategory).apply {
            adapter = ArrayAdapter.createFromResource(
                this@AddRecipeActivity,
                R.array.recipe_categories,
                android.R.layout.simple_spinner_item
            ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        }
    }
}
