package com.example.culinarycompanion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

// View Recipe File
class ViewRecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_recipe)

        // toolbar title = recipe name
        val title = intent.getStringExtra("title") ?: ""
        val category = intent.getStringExtra("category") ?: ""

        val tb = findViewById<MaterialToolbar>(R.id.viewToolbar)
        setSupportActionBar(tb)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tb.setNavigationOnClickListener { finish() }
        tb.title = title

        // populate
        findViewById<TextView>(R.id.tvTitle).text = title
        findViewById<TextView>(R.id.tvCategory).text = category

        // EDIT  -> open AddRecipeActivity pre-filled (not wired yet)
        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            startActivity(
                Intent(this, AddRecipeActivity::class.java)
                    .putExtra("edit", true)
                    .putExtra("title", title)
            )
        }

        // DELETE -> ask, then close (mock)
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.confirm_delete, title))
                .setPositiveButton(R.string.delete) { _, _ -> finish() }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }
}
