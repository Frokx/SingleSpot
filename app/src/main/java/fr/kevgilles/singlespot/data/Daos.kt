package fr.kevgilles.singlespot.data

import androidx.room.*

@Dao
interface LocationPointDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocationPointList(pointsList: MutableList<LocationPoint>): MutableList<Long>

    @Query("SELECT * FROM locationpoint")
    fun getAllLocation(): MutableList<LocationPoint>
}

@Dao
interface AreaDao {
    @Query("SELECT * FROM locationpoint WHERE location_area = :area")
    fun getAllLocation(area: String): MutableList<LocationPoint>
}