package fr.kevgilles.singlespot.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationPoint(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = LOCATION_ID)
    var id: Long = 0L,

    @ColumnInfo(name = LOCATION_LONG)
    var long: Double = 0.0,

    @ColumnInfo(name = LOCATION_LAT)
    var lat: Double = 0.0,

    @ColumnInfo(name = LOCATION_AREA)
    var area: String = ""
) {
    companion object {
        const val LOCATION_ID = "location_id"
        const val LOCATION_LONG = "location_longitude"
        const val LOCATION_LAT = "location_latitude"
        const val LOCATION_AREA = "location_area"
    }
}