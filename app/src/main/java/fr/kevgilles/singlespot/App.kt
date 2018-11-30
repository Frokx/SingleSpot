package fr.kevgilles.singlespot

import android.app.Application
import androidx.room.Room
import fr.kevgilles.singlespot.data.AppDatabase


class App: Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "init_database")
            .build()
    }
}