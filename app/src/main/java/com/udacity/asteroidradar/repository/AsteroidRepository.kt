package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.PodApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.*

enum class Filter { SAVED, WEEK, TODAY}

/**
 * providing a db in the construction is a dependency injection
 **/
class AsteroidRepository(private val database: AsteroidDatabase) {

    val today = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(
        Calendar.getInstance().time)

    private val _filter = MutableLiveData<Filter>()
    val filter
        get() = _filter

    val asteroids: LiveData<List<Asteroid>> =
        //Transformations.map allows us to convert from one live data to another
//        Transformations.map(database.asteroidDatabaseDao.getAllAsteroids()) {
//        it.asDomainModel()
        Transformations.switchMap(_filter) {
            val calender = Calendar.getInstance()
            calender.add(Calendar.DAY_OF_YEAR, 7)
            val week = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(
                calender.time)
            when(_filter.value) {
                Filter.TODAY -> Transformations.map(database.asteroidDatabaseDao.getTodayAsteroids(today)) { it.asDomainModel()}
                Filter.WEEK -> Transformations.map(database.asteroidDatabaseDao.getWeekAsteroids(week)) { it.asDomainModel()}
                else -> Transformations.map(database.asteroidDatabaseDao.getAllAsteroids()) { it.asDomainModel()}
            }
        }

    // method to refresh offline cash
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val response = AsteroidApi.retrofitService.getProperties(today, Constants.API_KEY).awaitResponse()
                val jsonBody = JSONObject(response.body())
                val listAst = parseAsteroidsJsonResult(jsonBody)
                database.asteroidDatabaseDao.insertAll(*listAst.asDatabaseObject())
            } catch (e: Exception) {
                Log.e("APICALL ERROR", e.message.toString())
            }

        }
    }

    val picOfDayEntity = database.asteroidDatabaseDao.getPicOfDay()
    val picOfDay  = Transformations.map(picOfDayEntity) {PictureOfDay(it.mediaType, it.title, it.url)}

    suspend fun refreshPicOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val picOfDay = PodApi.retrofitService.getProperties(Constants.API_KEY)
                database.asteroidDatabaseDao.insert(asDatabaseObject(picOfDay))
            } catch (e: Exception) {
                Log.e("APICALL ERROR POD", e.message.toString())
            }
        }

    }
}