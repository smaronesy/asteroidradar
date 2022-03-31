package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
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

/**
 * providing a db in the construction is a dependency injection
 **/
class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        //Transformation.map allows us to convert from one live data to another
        Transformations.map(database.asteroidDatabaseDao.getAllAsteroids()) {
        it.asDomainModel()
    }

    // method to refresh offline cash
    suspend fun refreshAsteroids() {
        val today = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(
            Calendar.getInstance().time)
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
    val picOfDay: LiveData<PictureOfDay> =
        Transformations.map(picOfDayEntity) {PictureOfDay(it.mediaType, it.title, it.url)}

    suspend fun refreshPicOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val picOfDay = PodApi.retrofitService.getProperties()
                database.asteroidDatabaseDao.insert(asDatabaseObject(picOfDay))
            } catch (e: Exception) {
                Log.e("APICALL ERROR POD", e.message.toString())
            }
        }
    }
}