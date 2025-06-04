package com.example.culinarycompanion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.culinarycompanion.data.RecipeViewModel
import com.example.culinarycompanion.db.Recipe
import com.google.android.material.appbar.MaterialToolbar

class ViewRecipeActivity : AppCompatActivity() {

    private val vm: RecipeViewModel by viewModels()
    private var recipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_recipe)

        val tb = findViewById<MaterialToolbar>(R.id.viewToolbar)
        setSupportActionBar(tb)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tb.setNavigationOnClickListener { finish() }

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvCategory = findViewById<TextView>(R.id.tvCategory)
        val tvIngredients = findViewById<TextView>(R.id.tvIngredients)
        val tvInstructions = findViewById<TextView>(R.id.tvInstructions)

        val id = intent.getLongExtra("id", 0L)
        vm.recipes.observe(this) { list ->
            recipe = list.firstOrNull { it.id == id } ?: return@observe
            recipe!!.apply {
                tb.title = title
                tvTitle.text = title
                tvCategory.text = category.name
                tvIngredients.text = ingredients
                tvInstructions.text = instructions
            }
        }

        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            recipe?.let {
                startActivity(Intent(this, AddRecipeActivity::class.java).putExtra("id", it.id))
            }
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            recipe?.let { r ->
                AlertDialog.Builder(this)
                    .setMessage(getString(R.string.confirm_delete, r.title))
                    .setPositiveButton(R.string.delete) { _, _ ->
                        vm.delete(r)
                        finish()
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            }
        }
    }
}
