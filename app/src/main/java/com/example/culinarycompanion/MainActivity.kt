package com.example.culinarycompanion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.culinarycompanion.data.RecipeViewModel
import com.example.culinarycompanion.db.Category
import com.example.culinarycompanion.db.Recipe
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val vm: RecipeViewModel by viewModels()
    private val adapter = RecipeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        findViewById<RecyclerView>(R.id.rvRecipes).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = this@MainActivity.adapter
        }

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, AddRecipeActivity::class.java))
        }

        // chip filtering
        val chips = findViewById<ChipGroup>(R.id.chipGroup)
        for (i in 0 until chips.childCount) {
            val chip = chips.getChildAt(i) as Chip
            chip.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    vm.setFilter(
                        when (chip.text.toString()) {
                            getString(R.string.chip_breakfast) -> Category.Breakfast
                            getString(R.string.chip_brunch) -> Category.Brunch
                            getString(R.string.chip_lunch) -> Category.Lunch
                            getString(R.string.chip_dinner) -> Category.Dinner
                            getString(R.string.chip_desserts) -> Category.Desserts
                            getString(R.string.chip_other) -> Category.Other
                            else -> null
                        }
                    )
                }
            }
        }

        vm.recipes.observe(this, Observer(adapter::submitList))
    }

    /** simple ListAdapter wrapper */
    private inner class RecipeAdapter :
        ListAdapter<Recipe, RecipeAdapter.VH>(object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(a: Recipe, b: Recipe) = a.id == b.id
            override fun areContentsTheSame(a: Recipe, b: Recipe) = a == b
        }) {

        inner class VH(v: View) : RecyclerView.ViewHolder(v) {
            val title: TextView = v.findViewById(R.id.tvRecipeTitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recipe, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val recipe = getItem(position)
            holder.title.text = recipe.title
            holder.itemView.setOnClickListener {
                startActivity(
                    Intent(this@MainActivity, ViewRecipeActivity::class.java)
                        .putExtra("id", recipe.id)
                )
            }
        }
    }
}
