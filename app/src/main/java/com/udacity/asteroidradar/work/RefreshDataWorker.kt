package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getInstance
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    // The doWork() function now returns Result instead of Payload because
    // they have combined Payload into Result.
    // Read more here - https://developer.android.com/jetpack/androidx/releases/work#1.0.0-alpha12
    override suspend fun doWork(): Result {
        val database = getInstance(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.refreshAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}