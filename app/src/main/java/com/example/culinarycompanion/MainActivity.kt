package com.example.culinarycompanion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val demoData = listOf(
        "Spaghetti Bolognese",
        "Pancakes",
        "Caesar Salad"
    )

    class RecipeVH(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.tvRecipeTitle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // “+” launches Add-screen
        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, AddRecipeActivity::class.java))
        }

        val rv = findViewById<RecyclerView>(R.id.rvRecipes)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = object : RecyclerView.Adapter<RecipeVH>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeVH {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recipe, parent, false)
                return RecipeVH(view)
            }

            override fun getItemCount() = demoData.size

            override fun onBindViewHolder(holder: RecipeVH, position: Int) {
                val title = demoData[position]
                holder.title.text = title
                holder.itemView.setOnClickListener {
                    startActivity(
                        Intent(this@MainActivity, ViewRecipeActivity::class.java)
                            .putExtra("title", title)
                            .putExtra("category", "Breakfast") // mock
                    )
                }
            }
        }
    }
}
