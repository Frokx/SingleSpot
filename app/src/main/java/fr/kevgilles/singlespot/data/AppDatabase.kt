package fr.kevgilles.singlespot.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocationPoint::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun LocationPointDao(): LocationPointDao
    abstract fun AreaDao(): AreaDao
}