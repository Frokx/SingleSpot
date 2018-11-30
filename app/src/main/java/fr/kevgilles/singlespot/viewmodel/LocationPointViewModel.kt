package fr.kevgilles.singlespot.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.kevgilles.singlespot.App
import fr.kevgilles.singlespot.data.LocationPoint
import java.util.concurrent.Executors

class LocationPointViewModel: ViewModel() {

    private var locations: MutableLiveData<List<LocationPoint>> = MutableLiveData()
    private val areaDao = App.database.AreaDao()

    fun getLocations() = locations

    /**
     * Fetch All points in specified Area
     */
    fun refreshLocationPoints(area: String) {
        val areaValue = areaTranslator(area)
        Executors.newSingleThreadExecutor().execute {
            locations.postValue(areaDao.getAllLocation(areaValue))
        }
    }

    /**
     * Transform area key into db language
     */
    private fun areaTranslator(area: String): String {
        return when(area) {
            "Area A" -> "a"
            "Area B" -> "b"
            "Area C" -> "c"
            "Area D" -> "d"
            "Area E" -> "e"
            "Area F" -> "f"
            "Area G" -> "g"
            "Area H" -> "h"
            "Area I" -> "i"
            "Area J" -> "j"
            else -> ""
        }
    }
}