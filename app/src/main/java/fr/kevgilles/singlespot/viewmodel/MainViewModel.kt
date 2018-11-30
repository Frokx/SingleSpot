package fr.kevgilles.singlespot.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.kevgilles.singlespot.App
import fr.kevgilles.singlespot.Constants
import fr.kevgilles.singlespot.HttpService
import fr.kevgilles.singlespot.data.LocationPoint
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class MainViewModel: ViewModel() {

    private var locationPointList: MutableList<LocationPoint> = ArrayList()
    private val locationPointIds: MutableList<Long> = ArrayList()
    private var readyToWork: MutableLiveData<Boolean> = MutableLiveData()
    private var done: Boolean = true

    // Database
    private val locationPointDao = App.database.LocationPointDao()

    // Work Trackers
    private var work: Int = 0
    private val interval = 100
    private var maxTest = 199

    fun isReadyToWork() = readyToWork
    fun isWorkOver() = done

    /**
     * Find id in Url
     */
    fun parsePointArray(pointsToParse: JSONArray) {
        done = false
        work = 0
        for (i in 0..(pointsToParse.length() - 1)) {
            val url = pointsToParse.getString(i)
            val splitUrl = url.split("/")
            locationPointIds.add(splitUrl[splitUrl.size - 1].toLong())
            if (pointsToParse.length() == locationPointIds.size) {
                readyToWork.postValue(true)
            }
        }
    }

    /**
     * If still has work to do, keep doing it, else: save what you fetched into db
     */
    fun keepWorking() {
        readyToWork.postValue(false)
        Log.e(Constants.TAG, "keepWorking()")
        if (work < maxTest) {
            fetchLatLongFromPoint(work, work + interval)
            Log.e(Constants.TAG, "New Interval: $work")
            work += interval
        }
        else {
            Log.e(Constants.TAG, "DONE")
            val executor = Executors.newSingleThreadExecutor()
            var inserted: MutableList<Long> = ArrayList()
            executor.submit { sortArea() }
            executor.submit { inserted = locationPointDao.insertLocationPointList(locationPointList) }
            executor.submit { Log.e(Constants.TAG, "Inserted " + inserted.size.toString()) } // test
            executor.submit { locationPointList.clear() } // test
//            executor.submit { maxTest += 200 }
//            executor.submit { isReadyToWork().postValue(true) } // To keep on saving
            executor.shutdown()
        }
    }

    /**
     * Sort Points by Location
     */
    private fun sortArea() {
        locationPointList.forEach {
            // A
            if (it.lat >= 50) {
                it.area = "a"
            }
            // B C D
            else if (it.lat < 50 && it.lat >= 47.5) {
                if (it.long >= -5 && it.long < 0) {
                    it.area = "b"
                }
                else if (it.long >= 0 && it.long < 5) {
                    it.area = "c"
                }
                else if (it.long >= 5 && it.long < 10) {
                    it.area = "d"
                }
            }
            // E F G
            else if (it.lat < 47.5 && it.lat >= 45) {
                if (it.long >= -5 && it.long < 0) {
                    it.area = "e"
                }
                else if (it.long >= 0 && it.long < 5) {
                    it.area = "f"
                }
                else if (it.long >= 5 && it.long < 10) {
                    it.area = "g"
                }
            }
            // H I J
            else if (it.lat < 45 && it.lat >= 40) {
                if (it.long >= -5 && it.long < 0) {
                    it.area = "h"
                }
                else if (it.long >= 0 && it.long < 5) {
                    it.area = "i"
                }
                else if (it.long >= 5 && it.long < 10) {
                    it.area = "j"
                }
            }
        }
    }

    /**
     * Parse API response into List of LocationPoint
     */
    private fun fetchLatLongFromPoint(min: Int, max: Int) {
        val workingArray = Arrays.copyOfRange(locationPointIds.toLongArray(), min, max)
        for (i in workingArray.indices) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.SINGLE_SPOT_SERVER_URL_POINTS)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

            val service: HttpService = retrofit.create(HttpService::class.java)
            val latLong: Call<String> = service.getLatLong(workingArray[i].toString())

            latLong.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val latLongObject = JSONObject(response.body())
                    locationPointList.add(
                        LocationPoint(
                            id = workingArray[i],
                            lat = latLongObject.getString("latitude").toDouble(),
                            long = latLongObject.getString("longitude").toDouble()
                        )
                    )
                    if (i % 50 == 0) { // Test Log
                        Log.e(Constants.TAG, locationPointList.size.toString())
                    }
                    if (locationPointList.size == interval || locationPointList.size == interval *2) {
                        readyToWork.postValue(true)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e(Constants.TAG, "Couldn't parse")
                }
            })
        }
    }


    /**
     * Get Points from API
     */
    fun getRemoteData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.SINGLE_SPOT_SERVER_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service: HttpService = retrofit.create(HttpService::class.java)
        val allLocationPoints: Call<String> = service.getAllLocationPoints()

        allLocationPoints.enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Executors.newSingleThreadExecutor().execute {
                    parsePointArray(JSONArray(response.body()))
                }
                Log.e(Constants.TAG, "Done Fetching")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(Constants.TAG, "Couldn't fetch data")
            }
        })
    }
}