package com.example.culinarycompanion

import android.app.Application
import androidx.room.Room
import com.example.culinarycompanion.db.AppDatabase

class CulinaryCompanionApp : Application() {
    // singleton DB seen by the whole app
    val db: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "recipes.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
